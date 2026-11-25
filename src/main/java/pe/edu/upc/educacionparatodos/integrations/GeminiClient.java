package pe.edu.upc.educacionparatodos.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import pe.edu.upc.educacionparatodos.exceptions.IaServiceException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

// Unico punto del backend que habla con la API de Gemini (HU-105, T03).
// La API key viaja solo en la cabecera x-goog-api-key desde el servidor:
// nunca se devuelve al frontend ni se escribe en los logs (CA09).
@Component
public class GeminiClient {

    private static final Logger log = LoggerFactory.getLogger(GeminiClient.class);

    private final String apiKey;
    private final String model;
    private final RestClient restClient;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public GeminiClient(@Value("${gemini.api-key:}") String apiKey,
                        @Value("${gemini.model}") String model,
                        @Value("${gemini.base-url}") String baseUrl,
                        @Value("${gemini.timeout-seconds:60}") long timeoutSeconds) {
        this.apiKey = apiKey;
        this.model = model;

        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        factory.setReadTimeout(Duration.ofSeconds(timeoutSeconds));

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    public boolean estaConfigurado() {
        return apiKey != null && !apiKey.isBlank();
    }

    // Envia las instrucciones y devuelve el texto que genera el modelo.
    // Se pide responseMimeType JSON para que la respuesta se pueda leer sin adivinar el formato.
    public String generarJson(String instruccionesSistema, String prompt) {
        if (!estaConfigurado()) {
            throw new IaServiceException(HttpStatus.SERVICE_UNAVAILABLE,
                    "El servicio de inteligencia artificial no está configurado. " +
                    "Pida al administrador que defina la variable de entorno GEMINI_API_KEY.");
        }

        Map<String, Object> cuerpo = Map.of(
                "systemInstruction", Map.of("parts", List.of(Map.of("text", instruccionesSistema))),
                "contents", List.of(Map.of(
                        "role", "user",
                        "parts", List.of(Map.of("text", prompt)))),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "temperature", 0.7));

        String respuesta;
        try {
            respuesta = restClient.post()
                    .uri("/models/{model}:generateContent", model)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo)
                    .retrieve()
                    .body(String.class);
        } catch (RestClientResponseException ex) {
            log.warn("Gemini respondio {} al generar ejercicios", ex.getStatusCode().value());
            throw new IaServiceException(HttpStatus.BAD_GATEWAY, mensajePorStatus(ex.getStatusCode().value()), ex);
        } catch (ResourceAccessException ex) {
            log.warn("No se pudo contactar a Gemini: {}", ex.getMessage());
            throw new IaServiceException(HttpStatus.GATEWAY_TIMEOUT,
                    "El servicio de inteligencia artificial no respondió a tiempo. Intente nuevamente en unos minutos.", ex);
        } catch (RestClientException ex) {
            log.warn("Error al llamar a Gemini: {}", ex.getMessage());
            throw new IaServiceException(HttpStatus.BAD_GATEWAY,
                    "No se pudo comunicar con el servicio de inteligencia artificial.", ex);
        }

        return extraerTexto(respuesta);
    }

    private String extraerTexto(String respuesta) {
        try {
            JsonNode raiz = jsonMapper.readTree(respuesta);
            JsonNode candidato = raiz.path("candidates").path(0);
            // Los modelos con razonamiento pueden devolver varias partes, algunas
            // marcadas como "thought": se juntan solo las partes de respuesta.
            StringBuilder sb = new StringBuilder();
            for (JsonNode parte : candidato.path("content").path("parts")) {
                if (!parte.path("thought").asBoolean(false)) {
                    sb.append(parte.path("text").asString(""));
                }
            }
            String texto = sb.toString();

            if (texto.isBlank()) {
                String motivo = candidato.path("finishReason").asString("");
                if (motivo.isBlank()) {
                    motivo = raiz.path("promptFeedback").path("blockReason").asString("");
                }
                log.warn("Gemini no devolvio texto. Motivo: {}", motivo);
                throw new IaServiceException(HttpStatus.BAD_GATEWAY,
                        "El servicio de inteligencia artificial no devolvió ejercicios. Intente con otros parámetros.");
            }
            return texto;
        } catch (JacksonException ex) {
            throw new IaServiceException(HttpStatus.BAD_GATEWAY,
                    "La respuesta del servicio de inteligencia artificial no tiene un formato válido.", ex);
        }
    }

    private String mensajePorStatus(int status) {
        return switch (status) {
            case 400, 401, 403 -> "El servicio de inteligencia artificial rechazó la solicitud. " +
                    "Verifique que la API key y el modelo configurados sean válidos.";
            case 404 -> "El modelo de inteligencia artificial configurado no existe. Revise la variable GEMINI_MODEL.";
            case 429 -> "Se alcanzó el límite de uso del servicio de inteligencia artificial. Intente nuevamente más tarde.";
            default -> "El servicio de inteligencia artificial no está disponible en este momento. Intente nuevamente más tarde.";
        };
    }
}
