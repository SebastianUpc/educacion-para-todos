package pe.edu.upc.educacionparatodos.servicesimplements;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pe.edu.upc.educacionparatodos.dtos.DashboardDTO;
import pe.edu.upc.educacionparatodos.dtos.EstudianteRendimientoDTO;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IProgresoRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * HU-106 T10: los indicadores del dashboard coinciden con los datos de origen.
 */
class DashboardServiceImplementTest {

    private IAlcanceAcademicoService alcance;
    private IEvaluacionRepository evaluacionRepository;
    private IDetalleEvaluacionRepository detalleRepository;
    private IProgresoRepository progresoRepository;
    private DashboardServiceImplement service;

    private Usuario docente;

    @BeforeEach
    void preparar() {
        alcance = mock(IAlcanceAcademicoService.class);
        evaluacionRepository = mock(IEvaluacionRepository.class);
        detalleRepository = mock(IDetalleEvaluacionRepository.class);
        progresoRepository = mock(IProgresoRepository.class);
        service = new DashboardServiceImplement(alcance, evaluacionRepository, detalleRepository, progresoRepository);

        Aula aula = new Aula();
        aula.setId(1L);
        aula.setGrado("5to");
        aula.setSeccion("A");
        Rol rol = new Rol();
        rol.setNombre("DOCENTE");
        docente = new Usuario();
        docente.setRol(rol);
        docente.setAula(aula);
        when(alcance.usuarioActual()).thenReturn(docente);
    }

    private Usuario estudiante(Long id, String nombre) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre(nombre);
        u.setApellido("Test");
        u.setAula(docente.getAula());
        return u;
    }

    @Test
    @DisplayName("CA03-CA06: calcula totales, promedio vigesimal, temas y estudiantes con dificultades")
    void calculaLosIndicadores() {
        when(alcance.estudiantesVisibles()).thenReturn(List.of(estudiante(1L, "Ana"), estudiante(2L, "Luis")));
        when(evaluacionRepository.countByEstudianteIdIn(anyCollection())).thenReturn(3L);
        when(evaluacionRepository.countByEstudianteIdInAndEstado(anyCollection(), eq("FINALIZADA"))).thenReturn(2L);
        when(evaluacionRepository.countByEstudianteIdInAndEstado(anyCollection(), eq("EN_CURSO"))).thenReturn(1L);

        // Ana: 9 de 10 (18.0). Luis: 2 de 10 (4.0). Grupo: 11 de 20 (11.0)
        when(detalleRepository.resumenPorEstudiante(anyCollection())).thenReturn(List.<Object[]>of(
                new Object[]{1L, 10L, 9L},
                new Object[]{2L, 10L, 2L}));
        when(detalleRepository.rendimientoPorTema(anyCollection())).thenReturn(List.<Object[]>of(
                new Object[]{5L, "Fracciones", 8L, 2L},
                new Object[]{6L, "Lectura", 12L, 9L}));
        when(detalleRepository.rendimientoPorEstudianteYTema(anyCollection())).thenReturn(List.<Object[]>of(
                new Object[]{2L, 5L, "Fracciones", 4L, 0L},
                new Object[]{2L, 6L, "Lectura", 6L, 2L}));
        when(progresoRepository.promedioAvance(anyCollection())).thenReturn(new BigDecimal("55.25"));
        when(progresoRepository.promedioAvancePorEstudiante(anyCollection())).thenReturn(List.<Object[]>of(
                new Object[]{1L, new BigDecimal("80")},
                new Object[]{2L, new BigDecimal("30.5")}));

        DashboardDTO dto = service.obtenerDashboard();

        assertThat(dto.getAlcance()).isEqualTo("Aula 5to A");
        assertThat(dto.isHayDatos()).isTrue();
        assertThat(dto.getTotalEstudiantes()).isEqualTo(2);
        assertThat(dto.getTotalEvaluaciones()).isEqualTo(3);
        assertThat(dto.getEvaluacionesFinalizadas()).isEqualTo(2);
        assertThat(dto.getTotalRespuestas()).isEqualTo(20);
        assertThat(dto.getPromedioAcademico()).isEqualTo(11.0);
        assertThat(dto.getPorcentajeAciertos()).isEqualTo(55.0);
        assertThat(dto.getProgresoPromedio()).isEqualTo(55.3);

        assertThat(dto.getRendimientoPorTema()).hasSize(2);
        assertThat(dto.getRendimientoPorTema().get(0).getPorcentajeAciertos()).isEqualTo(25.0);
        assertThat(dto.getRendimientoPorTema().get(1).getPromedio()).isEqualTo(15.0);

        // Solo Luis necesita refuerzo, con su tema mas debil
        assertThat(dto.getEstudiantesConDificultades()).hasSize(1);
        EstudianteRendimientoDTO luis = dto.getEstudiantesConDificultades().get(0);
        assertThat(luis.getIdEstudiante()).isEqualTo(2L);
        assertThat(luis.getPromedio()).isEqualTo(4.0);
        assertThat(luis.getTemaMasDebil()).isEqualTo("Fracciones");
        assertThat(luis.getMotivos()).hasSize(2);
    }

    @Test
    @DisplayName("CA08: sin estudiantes no se consulta la base de datos y se indica que no hay datos")
    void sinEstudiantes() {
        docente.setAula(null);
        when(alcance.estudiantesVisibles()).thenReturn(List.of());

        DashboardDTO dto = service.obtenerDashboard();

        assertThat(dto.isHayDatos()).isFalse();
        assertThat(dto.getAlcance()).isEqualTo("Sin aula asignada");
        assertThat(dto.getRendimientoPorTema()).isEmpty();
        assertThat(dto.getEstudiantesConDificultades()).isEmpty();
        verifyNoInteractions(detalleRepository, evaluacionRepository, progresoRepository);
    }
}
