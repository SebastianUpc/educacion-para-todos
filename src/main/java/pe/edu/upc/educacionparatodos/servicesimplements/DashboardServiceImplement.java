package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.educacionparatodos.dtos.DashboardDTO;
import pe.edu.upc.educacionparatodos.dtos.EstudianteRendimientoDTO;
import pe.edu.upc.educacionparatodos.dtos.RendimientoTemaDTO;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IEvaluacionRepository;
import pe.edu.upc.educacionparatodos.repositories.IProgresoRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IDashboardService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// HU-106. Indicadores elegidos (T01):
//  - Promedio academico: % de respuestas correctas llevado a escala vigesimal (0-20).
//  - Rendimiento por tema: % de aciertos de todo el grupo en cada tema.
//  - Dificultades: promedio menor a 11 (nota desaprobatoria) o avance menor al 50%.
@Service
public class DashboardServiceImplement implements IDashboardService {

    private static final double NOTA_APROBATORIA = 11.0;
    private static final double AVANCE_MINIMO = 50.0;
    private static final String ESTADO_FINALIZADA = "FINALIZADA";
    private static final String ESTADO_EN_CURSO = "EN_CURSO";

    private final IAlcanceAcademicoService alcanceService;
    private final IEvaluacionRepository evaluacionRepository;
    private final IDetalleEvaluacionRepository detalleRepository;
    private final IProgresoRepository progresoRepository;

    public DashboardServiceImplement(IAlcanceAcademicoService alcanceService,
                                     IEvaluacionRepository evaluacionRepository,
                                     IDetalleEvaluacionRepository detalleRepository,
                                     IProgresoRepository progresoRepository) {
        this.alcanceService = alcanceService;
        this.evaluacionRepository = evaluacionRepository;
        this.detalleRepository = detalleRepository;
        this.progresoRepository = progresoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDTO obtenerDashboard() {
        Usuario actual = alcanceService.usuarioActual();
        List<Usuario> estudiantes = alcanceService.estudiantesVisibles();

        DashboardDTO dto = new DashboardDTO();
        dto.setAlcance(describirAlcance(actual));
        dto.setNotaMinimaAprobatoria(NOTA_APROBATORIA);
        dto.setTotalEstudiantes(estudiantes.size());
        dto.setRendimientoPorTema(List.of());
        dto.setEstudiantesConDificultades(List.of());

        // Con una lista vacia el "in :ids" no es valido en todas las bases de datos
        if (estudiantes.isEmpty()) {
            dto.setHayDatos(false);
            return dto;
        }

        List<Long> ids = estudiantes.stream().map(Usuario::getId).toList();

        dto.setTotalEvaluaciones(evaluacionRepository.countByEstudianteIdIn(ids));
        dto.setEvaluacionesFinalizadas(evaluacionRepository.countByEstudianteIdInAndEstado(ids, ESTADO_FINALIZADA));
        dto.setEvaluacionesEnCurso(evaluacionRepository.countByEstudianteIdInAndEstado(ids, ESTADO_EN_CURSO));

        // --- Respuestas por estudiante: [idEstudiante, total, correctas]
        Map<Long, long[]> respuestasPorEstudiante = new HashMap<>();
        long totalRespuestas = 0;
        long totalCorrectas = 0;
        for (Object[] fila : detalleRepository.resumenPorEstudiante(ids)) {
            long total = numero(fila[1]);
            long correctas = numero(fila[2]);
            respuestasPorEstudiante.put((Long) fila[0], new long[]{total, correctas});
            totalRespuestas += total;
            totalCorrectas += correctas;
        }
        dto.setTotalRespuestas(totalRespuestas);
        dto.setPorcentajeAciertos(porcentaje(totalCorrectas, totalRespuestas));
        dto.setPromedioAcademico(nota(totalCorrectas, totalRespuestas));

        // --- Progreso
        dto.setProgresoPromedio(redondear(progresoRepository.promedioAvance(ids)));
        Map<Long, Double> progresoPorEstudiante = new HashMap<>();
        for (Object[] fila : progresoRepository.promedioAvancePorEstudiante(ids)) {
            progresoPorEstudiante.put((Long) fila[0], redondear(fila[1]));
        }

        // --- Rendimiento por tema: [idTema, nombre, total, correctas]
        List<RendimientoTemaDTO> porTema = new ArrayList<>();
        for (Object[] fila : detalleRepository.rendimientoPorTema(ids)) {
            long total = numero(fila[2]);
            long correctas = numero(fila[3]);
            RendimientoTemaDTO tema = new RendimientoTemaDTO();
            tema.setIdTema((Long) fila[0]);
            tema.setTema((String) fila[1]);
            tema.setTotalRespuestas(total);
            tema.setRespuestasCorrectas(correctas);
            tema.setPorcentajeAciertos(porcentaje(correctas, total));
            tema.setPromedio(nota(correctas, total));
            porTema.add(tema);
        }
        dto.setRendimientoPorTema(porTema);

        // --- Tema mas debil de cada estudiante: [idEstudiante, idTema, nombre, total, correctas]
        Map<Long, String> temaMasDebil = new HashMap<>();
        Map<Long, Double> peorPorcentaje = new HashMap<>();
        for (Object[] fila : detalleRepository.rendimientoPorEstudianteYTema(ids)) {
            Long idEstudiante = (Long) fila[0];
            Double pct = porcentaje(numero(fila[4]), numero(fila[3]));
            if (pct != null && pct < peorPorcentaje.getOrDefault(idEstudiante, Double.MAX_VALUE)) {
                peorPorcentaje.put(idEstudiante, pct);
                temaMasDebil.put(idEstudiante, (String) fila[2]);
            }
        }

        // --- Estudiantes que necesitan refuerzo (CA06)
        List<EstudianteRendimientoDTO> conDificultades = new ArrayList<>();
        for (Usuario estudiante : estudiantes) {
            long[] resp = respuestasPorEstudiante.getOrDefault(estudiante.getId(), new long[]{0, 0});
            Double promedio = nota(resp[1], resp[0]);
            Double progreso = progresoPorEstudiante.get(estudiante.getId());

            List<String> motivos = new ArrayList<>();
            if (promedio != null && promedio < NOTA_APROBATORIA) {
                motivos.add("Promedio desaprobatorio (" + promedio + ")");
            }
            if (progreso != null && progreso < AVANCE_MINIMO) {
                motivos.add("Avance bajo (" + progreso + "%)");
            }
            if (motivos.isEmpty()) {
                continue;
            }

            EstudianteRendimientoDTO item = new EstudianteRendimientoDTO();
            item.setIdEstudiante(estudiante.getId());
            item.setNombreCompleto(estudiante.getNombre() + " " + estudiante.getApellido());
            item.setAula(estudiante.getAula() != null
                    ? estudiante.getAula().getGrado() + " " + estudiante.getAula().getSeccion() : null);
            item.setTotalRespuestas(resp[0]);
            item.setPromedio(promedio);
            item.setPorcentajeAciertos(porcentaje(resp[1], resp[0]));
            item.setProgresoPromedio(progreso);
            item.setTemaMasDebil(temaMasDebil.get(estudiante.getId()));
            item.setMotivos(motivos);
            conDificultades.add(item);
        }
        // Primero los que estan peor: sin promedio al final
        conDificultades.sort(Comparator.comparing(EstudianteRendimientoDTO::getPromedio,
                Comparator.nullsLast(Comparator.naturalOrder())));
        dto.setEstudiantesConDificultades(conDificultades);

        dto.setHayDatos(totalRespuestas > 0 || dto.getProgresoPromedio() != null || dto.getTotalEvaluaciones() > 0);
        return dto;
    }

    private String describirAlcance(Usuario actual) {
        boolean esDocente = actual.getRol() != null
                && "DOCENTE".equalsIgnoreCase(actual.getRol().getNombre());
        if (!esDocente) {
            return "Todas las aulas";
        }
        if (actual.getAula() == null) {
            return "Sin aula asignada";
        }
        return "Aula " + actual.getAula().getGrado() + " " + actual.getAula().getSeccion();
    }

    private long numero(Object valor) {
        return valor == null ? 0 : ((Number) valor).longValue();
    }

    private Double porcentaje(long correctas, long total) {
        if (total == 0) {
            return null;
        }
        return redondear(correctas * 100.0 / total);
    }

    private Double nota(long correctas, long total) {
        if (total == 0) {
            return null;
        }
        return redondear(correctas * 20.0 / total);
    }

    private Double redondear(Object valor) {
        if (valor == null) {
            return null;
        }
        return new BigDecimal(valor.toString()).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }
}
