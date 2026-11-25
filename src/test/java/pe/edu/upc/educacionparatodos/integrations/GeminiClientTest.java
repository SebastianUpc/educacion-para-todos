package pe.edu.upc.educacionparatodos.integrations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import pe.edu.upc.educacionparatodos.exceptions.IaServiceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * HU-105 CA08 y CA09: sin API key no se llama a la IA y el mensaje no expone credenciales.
 */
class GeminiClientTest {

    @Test
    @DisplayName("Sin GEMINI_API_KEY responde 503 con un mensaje para el docente")
    void sinApiKey() {
        GeminiClient client = new GeminiClient("", "gemini-3.6-flash", "http://localhost:1", 5);

        assertThat(client.estaConfigurado()).isFalse();
        assertThatThrownBy(() -> client.generarJson("sistema", "prompt"))
                .isInstanceOf(IaServiceException.class)
                .hasMessageContaining("no está configurado")
                .satisfies(ex -> assertThat(((IaServiceException) ex).getStatus())
                        .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE));
    }

    @Test
    @DisplayName("Si la API no responde, el error no incluye la API key")
    void servidorCaido() {
        GeminiClient client = new GeminiClient("clave-secreta-123", "gemini-3.6-flash", "http://127.0.0.1:1", 2);

        assertThatThrownBy(() -> client.generarJson("sistema", "prompt"))
                .isInstanceOf(IaServiceException.class)
                .satisfies(ex -> assertThat(ex.getMessage()).doesNotContain("clave-secreta-123"));
    }
}
