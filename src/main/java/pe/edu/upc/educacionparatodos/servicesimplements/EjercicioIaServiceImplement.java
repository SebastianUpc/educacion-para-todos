package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaRequestDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaResponseDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioInsertDTO;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.entities.Progreso;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.IaServiceException;
import pe.edu.upc.educacionparatodos.integrations.GeminiClient;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IProgresoRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioIaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EjercicioIaServiceImplement implements IEjercicioIaService {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final int MAX_ERRORES_EN_PROMPT = 5;

    // T04: instrucciones fijas del modelo. El formato de salida coincide con el
    // de la tabla ejercicios: enunciado, alternativas (contenidoJson) y respuesta.
    private static final String INSTRUCCIONES_SISTEMA = """
            Eres un docente experto de educacion basica regular del Peru que prepara
            ejercicios de refuerzo personalizados para un estudiante concreto.

            Reglas:
            - Escribe en espanol claro y adecuado al grado del estudiante.
            - Cada ejercicio trata solo sobre el tema indicado.
            - Ajusta la dificultad al nivel pedido (1 = muy basico, 5 = avanzado).
            - Si se informan errores previos, crea ejercicios que ataquen esas confusiones.
            - Cada ejercicio tiene exactamente 4 alternativas y una sola correcta.
            - "respuestaCorrecta" debe ser identica, caracter por caracter, a una de las alternativas.
            - "explicacion" justifica la respuesta en una o dos oraciones.
            - No incluyas datos personales del estudiante en los enunciados.

            Responde UNICAMENTE con un JSON con esta forma exacta:
            {"ejercicios":[{"enunciado":"...","alternativas":["...","...","...","..."],
              "respuestaCorrecta":"...","explicacion":"...","nivelDificultad":1}]}
            """;

    private final GeminiClient geminiClient;
    private final IAlcanceAcademicoService alcanceService;
    private final ITemaService temaService;
    private final IEjercicioService ejercicioService;
    private final IProgresoRepository progresoRepository;
    private final IDetalleEvaluacionRepository detalleRepository;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public EjercicioIaServiceImplement(GeminiClient geminiClient,
                                       IAlcanceAcademicoService alcanceService,
                                       ITemaService temaService,
                                       IEjercicioService ejercicioService,
                                       IProgresoRepository progresoRepository,
                                       IDetalleEvaluacionRepository detalleRepository) {
        this.geminiClient = geminiClient;
        this.alcanceService = alcanceService;
        this.temaService = temaService;
        this.ejercicioService = ejercicioService;
        this.progresoRepository = progresoRepository;
        this.detalleRepository = detalleRepository;
    }

    // Sin @Transactional a proposito: la llamada a la IA puede tardar varios
    // segundos y no debe mantener ocupada una conexion de la base de datos.
    @Override
    public EjercicioIaResponseDTO generar(EjercicioIaRequestDTO request) {
        Usuario estudiante = alcanceService.validarEstudianteVisible(request.getIdEstudiante());
        Tema tema = temaService.findById(request.getIdTema());

        // T06: se consulta lo que la plataforma sabe del estudiante en este tema
        Optional<Progreso> progreso = progresoRepository
                .findFirstByEstudianteIdAndTemaIdOrderByUpdatedAtDesc(estudiante.getId(), tema.getId());
        long[] aciertos = aciertosEnTema(estudiante.getId(), tema.getId());
        List<DetalleEvaluacion> errores = detalleRepository.erroresRecientes(
                estudiante.getId(), tema.getId(), PageRequest.of(0, MAX_ERRORES_EN_PROMPT));

        int nivel = decidirNivel(request.getNivelDificultad(), progreso, aciertos);
        List<String> contexto = armarContexto(estudiante, progreso, aciertos, errores, request.getNecesidades());

        String prompt = armarPrompt(tema, nivel, request.getCantidad(), contexto);
        String respuestaIa = geminiClient.generarJson(INSTRUCCIONES_SISTEMA, prompt);

        List<EjercicioInsertDTO> ejercicios = convertirRespuesta(respuestaIa, tema.getId(), nivel, request.getCantidad());

        EjercicioIaResponseDTO dto = new EjercicioIaResponseDTO();
        dto.setIdEstudiante(estudiante.getId());
        dto.setNombreEstudiante(estudiante.getNombre() + " " + estudiante.getApellido());
        dto.setIdTema(tema.getId());
        dto.setNombreTema(tema.getNombre());
        dto.setNivelAplicado(nivel);
        dto.setContextoUsado(contexto);
        dto.setEjercicios(ejercicios);
        return dto;
    }

    // T10 / CA07: se guardan solo los ejercicios que el docente aprobo, siempre
    // marcados como generados por IA aunque el cliente envie otra cosa.
    @Override
    @Transactional
    public List<Ejercicio> guardar(List<EjercicioInsertDTO> ejercicios) {
        List<Ejercicio> guardados = new ArrayList<>();
        for (EjercicioInsertDTO dto : ejercicios) {
            Tema tema = new Tema();
            tema.setId(dto.getIdTema());

            Ejercicio ejercicio = new Ejercicio(
                    tema,
                    dto.getEnunciado().trim(),
                    dto.getContenidoJson(),
                    dto.getRespuestaCorrecta().trim(),
                    dto.getNivelDificultad(),
                    true,
                    dto.getEstado() == null || dto.getEstado().isBlank() ? ESTADO_ACTIVO : dto.getEstado());

            guardados.add(ejercicioService.insert(ejercicio));
        }
        return guardados;
    }

    // Fila: [idEstudiante, idTema, nombreTema, total, correctas]
    private long[] aciertosEnTema(Long idEstudiante, Long idTema) {
        return detalleRepository.rendimientoPorEstudianteYTema(List.of(idEstudiante))
                .stream()
                .filter(fila -> idTema.equals(fila[1]))
                .findFirst()
                .map(fila -> new long[]{((Number) fila[3]).longValue(), ((Number) fila[4]).longValue()})
                .orElse(new long[]{0, 0});
    }

    // Prioridad: lo que pide el docente > nivel del progreso > lo que sugieren sus aciertos
    private int decidirNivel(Integer pedido, Optional<Progreso> progreso, long[] aciertos) {
        if (pedido != null) {
            return pedido;
        }
        if (progreso.isPresent() && progreso.get().getNivelActual() != null) {
            return Math.max(1, Math.min(5, progreso.get().getNivelActual()));
        }
        if (aciertos[0] > 0) {
            double porcentaje = aciertos[1] * 100.0 / aciertos[0];
            if (porcentaje < 40) return 1;
            if (porcentaje < 60) return 2;
            if (porcentaje < 80) return 3;
            return 4;
        }
        return 2;
    }

    private List<String> armarContexto(Usuario estudiante, Optional<Progreso> progreso, long[] aciertos,
                                       List<DetalleEvaluacion> errores, String necesidades) {
        List<String> contexto = new ArrayList<>();

        if (estudiante.getAula() != null) {
            contexto.add("Grado y sección: " + estudiante.getAula().getGrado() + " " + estudiante.getAula().getSeccion());
        }

        progreso.ifPresentOrElse(p -> {
            contexto.add("Progreso registrado en el tema: nivel " + p.getNivelActual()
                    + ", avance " + p.getPorcentajeAvance() + "%");
            if (p.getDiagnosticoIa() != null && !p.getDiagnosticoIa().isBlank()) {
                contexto.add("Diagnóstico previo: " + p.getDiagnosticoIa());
            }
        }, () -> contexto.add("Sin progreso registrado en este tema"));

        if (aciertos[0] > 0) {
            long porcentaje = Math.round(aciertos[1] * 100.0 / aciertos[0]);
            contexto.add("Resultados en el tema: " + aciertos[1] + " de " + aciertos[0]
                    + " respuestas correctas (" + porcentaje + "%)");
        } else {
            contexto.add("Aún no tiene respuestas evaluadas en este tema");
        }

        for (DetalleEvaluacion error : errores) {
            contexto.add("Falló: \"" + recortar(error.getEjercicio().getEnunciado(), 200)
                    + "\" (respondió \"" + recortar(error.getRespuesta(), 80)
                    + "\", correcta: \"" + recortar(error.getEjercicio().getRespuestaCorrecta(), 80) + "\")");
        }

        if (necesidades != null && !necesidades.isBlank()) {
            contexto.add("Necesidades indicadas por el docente: " + necesidades.trim());
        }

        return contexto;
    }

    private String armarPrompt(Tema tema, int nivel, int cantidad, List<String> contexto) {
        StringBuilder sb = new StringBuilder();
        sb.append("Genera ").append(cantidad).append(" ejercicio(s) de refuerzo.\n");
        sb.append("Materia: ").append(tema.getMateria()).append("\n");
        sb.append("Tema: ").append(tema.getNombre()).append("\n");
        sb.append("Nivel de dificultad: ").append(nivel).append(" de 5\n\n");
        sb.append("Información del estudiante:\n");
        for (String linea : contexto) {
            sb.append("- ").append(linea).append("\n");
        }
        return sb.toString();
    }

    // CA05: se valida que cada ejercicio tenga lo que la tabla ejercicios necesita
    private List<EjercicioInsertDTO> convertirRespuesta(String texto, Long idTema, int nivel, int cantidad) {
        JsonNode raiz;
        try {
            raiz = jsonMapper.readTree(quitarBloqueDeCodigo(texto));
        } catch (JacksonException ex) {
            throw new IaServiceException(HttpStatus.BAD_GATEWAY,
                    "La inteligencia artificial devolvió ejercicios con un formato inválido. Intente nuevamente.", ex);
        }

        JsonNode lista = raiz.isArray() ? raiz : raiz.path("ejercicios");
        List<EjercicioInsertDTO> resultado = new ArrayList<>();

        for (JsonNode item : lista) {
            if (resultado.size() >= cantidad) {
                break;
            }

            String enunciado = item.path("enunciado").asString("").trim();
            String correcta = item.path("respuestaCorrecta").asString("").trim();
            if (enunciado.isEmpty() || correcta.isEmpty()) {
                continue;
            }

            ArrayNode alternativas = jsonMapper.createArrayNode();
            boolean contieneCorrecta = false;
            for (JsonNode alt : item.path("alternativas")) {
                String valor = alt.asString("").trim();
                if (!valor.isEmpty()) {
                    alternativas.add(valor);
                    contieneCorrecta |= valor.equals(correcta);
                }
            }
            // Si la IA no repitio la respuesta entre las alternativas, se agrega
            // para que el ejercicio siempre se pueda resolver.
            if (!alternativas.isEmpty() && !contieneCorrecta) {
                alternativas.add(correcta);
            }

            ObjectNode contenido = jsonMapper.createObjectNode();
            contenido.set("alternativas", alternativas);
            String explicacion = item.path("explicacion").asString("").trim();
            if (!explicacion.isEmpty()) {
                contenido.put("explicacion", explicacion);
            }

            int nivelItem = item.path("nivelDificultad").asInt(nivel);

            EjercicioInsertDTO dto = new EjercicioInsertDTO();
            dto.setIdTema(idTema);
            dto.setEnunciado(enunciado);
            dto.setContenidoJson(jsonMapper.writeValueAsString(contenido));
            dto.setRespuestaCorrecta(correcta);
            dto.setNivelDificultad(nivelItem < 1 || nivelItem > 5 ? nivel : nivelItem);
            dto.setGeneradoPorIa(true);
            dto.setEstado(ESTADO_ACTIVO);
            resultado.add(dto);
        }

        if (resultado.isEmpty()) {
            throw new IaServiceException(HttpStatus.BAD_GATEWAY,
                    "La inteligencia artificial no devolvió ejercicios válidos. Intente nuevamente.");
        }
        return resultado;
    }

    // Algunos modelos envuelven el JSON en ```json ... ``` aunque se pida JSON puro
    private String quitarBloqueDeCodigo(String texto) {
        String t = texto.trim();
        if (t.startsWith("```")) {
            int inicio = t.indexOf('\n');
            int fin = t.lastIndexOf("```");
            if (inicio > 0 && fin > inicio) {
                return t.substring(inicio + 1, fin).trim();
            }
        }
        return t;
    }

    private String recortar(String texto, int max) {
        if (texto == null) {
            return "";
        }
        String limpio = texto.replaceAll("\\s+", " ").trim();
        return limpio.length() <= max ? limpio : limpio.substring(0, max) + "...";
    }
}
