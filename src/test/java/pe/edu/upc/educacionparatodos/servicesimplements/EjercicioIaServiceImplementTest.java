package pe.edu.upc.educacionparatodos.servicesimplements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaRequestDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaResponseDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioInsertDTO;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.entities.Progreso;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.IaServiceException;
import pe.edu.upc.educacionparatodos.integrations.GeminiClient;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IProgresoRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * HU-105 T11: personalizacion, formato y almacenamiento de los ejercicios de la IA.
 * La API de Gemini se reemplaza por un doble de prueba: no se gasta cuota ni se
 * necesita la API key para ejecutar estas pruebas.
 */
class EjercicioIaServiceImplementTest {

    private GeminiClient gemini;
    private IAlcanceAcademicoService alcance;
    private ITemaService temaService;
    private IEjercicioService ejercicioService;
    private IProgresoRepository progresoRepository;
    private IDetalleEvaluacionRepository detalleRepository;
    private EjercicioIaServiceImplement service;

    @BeforeEach
    void preparar() {
        gemini = mock(GeminiClient.class);
        alcance = mock(IAlcanceAcademicoService.class);
        temaService = mock(ITemaService.class);
        ejercicioService = mock(IEjercicioService.class);
        progresoRepository = mock(IProgresoRepository.class);
        detalleRepository = mock(IDetalleEvaluacionRepository.class);

        service = new EjercicioIaServiceImplement(gemini, alcance, temaService, ejercicioService,
                progresoRepository, detalleRepository);

        Aula aula = new Aula();
        aula.setGrado("5to");
        aula.setSeccion("A");
        Usuario estudiante = new Usuario();
        estudiante.setId(7L);
        estudiante.setNombre("Ana");
        estudiante.setApellido("Quispe");
        estudiante.setAula(aula);
        when(alcance.validarEstudianteVisible(7L)).thenReturn(estudiante);

        Tema tema = new Tema("Matematica", "Fracciones");
        tema.setId(3L);
        when(temaService.findById(3L)).thenReturn(tema);

        Progreso progreso = new Progreso();
        progreso.setNivelActual(2);
        progreso.setPorcentajeAvance(new BigDecimal("35.00"));
        progreso.setDiagnosticoIa("Confunde numerador y denominador");
        when(progresoRepository.findFirstByEstudianteIdAndTemaIdOrderByUpdatedAtDesc(7L, 3L))
                .thenReturn(Optional.of(progreso));

        // 1 de 4 respuestas correctas en el tema
        when(detalleRepository.rendimientoPorEstudianteYTema(anyList()))
                .thenReturn(List.<Object[]>of(new Object[]{7L, 3L, "Fracciones", 4L, 1L}));
        when(detalleRepository.erroresRecientes(eq(7L), eq(3L), any(Pageable.class))).thenReturn(List.of());
    }

    private EjercicioIaRequestDTO solicitud(int cantidad) {
        EjercicioIaRequestDTO dto = new EjercicioIaRequestDTO();
        dto.setIdEstudiante(7L);
        dto.setIdTema(3L);
        dto.setCantidad(cantidad);
        dto.setNecesidades("Usar ejemplos con pizzas");
        return dto;
    }

    @Test
    @DisplayName("CA03: el prompt incluye el progreso, los resultados y las necesidades del estudiante")
    void elPromptConsideraLaInformacionDelEstudiante() {
        when(gemini.generarJson(anyString(), anyString())).thenReturn("""
                {"ejercicios":[{"enunciado":"Cuanto es 1/2 + 1/4?","alternativas":["3/4","2/6","1/8","2/4"],
                 "respuestaCorrecta":"3/4","explicacion":"Se busca denominador comun","nivelDificultad":2}]}
                """);

        EjercicioIaResponseDTO respuesta = service.generar(solicitud(1));

        ArgumentCaptor<String> prompt = ArgumentCaptor.forClass(String.class);
        verify(gemini).generarJson(anyString(), prompt.capture());

        assertThat(prompt.getValue())
                .contains("Fracciones")
                .contains("Nivel de dificultad: 2 de 5")
                .contains("avance 35.00%")
                .contains("Confunde numerador y denominador")
                .contains("1 de 4 respuestas correctas")
                .contains("Usar ejemplos con pizzas")
                .doesNotContain("Ana")
                .doesNotContain("Quispe");
        assertThat(respuesta.getNivelAplicado()).isEqualTo(2);
        assertThat(respuesta.getContextoUsado()).isNotEmpty();
    }

    @Test
    @DisplayName("CA05: cada ejercicio trae enunciado, alternativas en contenidoJson y respuesta correcta")
    void convierteLaRespuestaAlFormatoDeLaPlataforma() {
        // Viene envuelto en ```json y con una alternativa que no incluye la correcta
        when(gemini.generarJson(anyString(), anyString())).thenReturn("""
                ```json
                {"ejercicios":[
                  {"enunciado":"Simplifica 2/4","alternativas":["1/3","2/3","1/4"],"respuestaCorrecta":"1/2","nivelDificultad":9},
                  {"enunciado":"","alternativas":["a"],"respuestaCorrecta":"a"},
                  {"enunciado":"Extra","alternativas":["x","y"],"respuestaCorrecta":"x"}
                ]}
                ```
                """);

        List<EjercicioInsertDTO> ejercicios = service.generar(solicitud(2)).getEjercicios();

        // El ejercicio sin enunciado se descarta y se respeta la cantidad pedida
        assertThat(ejercicios).hasSize(2);
        EjercicioInsertDTO primero = ejercicios.get(0);
        assertThat(primero.getIdTema()).isEqualTo(3L);
        assertThat(primero.getRespuestaCorrecta()).isEqualTo("1/2");
        assertThat(primero.getContenidoJson()).contains("\"alternativas\"").contains("1/2");
        assertThat(primero.getNivelDificultad()).isEqualTo(2); // 9 fuera de rango -> nivel aplicado
        assertThat(primero.getGeneradoPorIa()).isTrue();
        assertThat(primero.getEstado()).isEqualTo("ACTIVO");
    }

    @Test
    @DisplayName("CA08: si la IA devuelve algo que no es JSON se informa con un error 502")
    void respuestaInvalidaDeLaIa() {
        when(gemini.generarJson(anyString(), anyString())).thenReturn("Lo siento, no puedo ayudar");

        assertThatThrownBy(() -> service.generar(solicitud(1)))
                .isInstanceOf(IaServiceException.class)
                .satisfies(ex -> assertThat(((IaServiceException) ex).getStatus()).isEqualTo(HttpStatus.BAD_GATEWAY));
    }

    @Test
    @DisplayName("CA08: el error de comunicacion con la IA llega al docente tal cual")
    void errorDeComunicacionSePropaga() {
        when(gemini.generarJson(anyString(), anyString())).thenThrow(
                new IaServiceException(HttpStatus.SERVICE_UNAVAILABLE, "no configurado"));

        assertThatThrownBy(() -> service.generar(solicitud(1)))
                .isInstanceOf(IaServiceException.class)
                .hasMessage("no configurado");
    }

    @Test
    @DisplayName("CA07: al guardar siempre se marca generadoPorIa = true")
    void guardarMarcaGeneradoPorIa() {
        when(ejercicioService.insert(any(Ejercicio.class))).thenAnswer(inv -> inv.getArgument(0));

        EjercicioInsertDTO dto = new EjercicioInsertDTO();
        dto.setIdTema(3L);
        dto.setEnunciado("  Cuanto es 1/2 + 1/2?  ");
        dto.setRespuestaCorrecta("1");
        dto.setNivelDificultad(1);
        dto.setGeneradoPorIa(false); // el cliente intenta desmarcarlo

        List<Ejercicio> guardados = service.guardar(List.of(dto));

        assertThat(guardados).hasSize(1);
        assertThat(guardados.get(0).getGeneradoPorIa()).isTrue();
        assertThat(guardados.get(0).getEstado()).isEqualTo("ACTIVO");
        assertThat(guardados.get(0).getEnunciado()).isEqualTo("Cuanto es 1/2 + 1/2?");
    }
}
