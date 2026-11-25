package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;

import java.util.Collection;
import java.util.List;

@Repository
public interface IDetalleEvaluacionRepository extends JpaRepository<DetalleEvaluacion, Long> {

    List<DetalleEvaluacion> findByEvaluacionId(Long evaluacionId);

    boolean existsByEjercicioId(Long ejercicioId);

    boolean existsByEvaluacionId(Long evaluacionId);

    // Dashboard: respuestas totales y correctas de cada estudiante.
    // Fila: [idEstudiante, total, correctas]
    @Query("select d.evaluacion.estudiante.id, count(d), " +
            " sum(case when d.esCorrecta = true then 1 else 0 end)" +
            " from DetalleEvaluacion d" +
            " where d.evaluacion.estudiante.id in :ids" +
            " group by d.evaluacion.estudiante.id")
    List<Object[]> resumenPorEstudiante(@Param("ids") Collection<Long> ids);

    // Dashboard: rendimiento por tema de un grupo de estudiantes.
    // Fila: [idTema, nombreTema, total, correctas]
    @Query("select t.id, t.nombre, count(d), " +
            " sum(case when d.esCorrecta = true then 1 else 0 end)" +
            " from DetalleEvaluacion d join d.ejercicio e join e.tema t" +
            " where d.evaluacion.estudiante.id in :ids" +
            " group by t.id, t.nombre" +
            " order by t.nombre")
    List<Object[]> rendimientoPorTema(@Param("ids") Collection<Long> ids);

    // Dashboard e IA: rendimiento de cada estudiante en cada tema.
    // Fila: [idEstudiante, idTema, nombreTema, total, correctas]
    @Query("select d.evaluacion.estudiante.id, t.id, t.nombre, count(d), " +
            " sum(case when d.esCorrecta = true then 1 else 0 end)" +
            " from DetalleEvaluacion d join d.ejercicio e join e.tema t" +
            " where d.evaluacion.estudiante.id in :ids" +
            " group by d.evaluacion.estudiante.id, t.id, t.nombre")
    List<Object[]> rendimientoPorEstudianteYTema(@Param("ids") Collection<Long> ids);

    // IA: ultimos ejercicios que el estudiante fallo en un tema, para que la IA
    // sepa en que se equivoca y genere ejercicios de refuerzo sobre eso.
    @Query("select d from DetalleEvaluacion d" +
            " where d.evaluacion.estudiante.id = :idEstudiante" +
            " and d.ejercicio.tema.id = :idTema" +
            " and d.esCorrecta = false" +
            " order by d.fechaRespuesta desc")
    List<DetalleEvaluacion> erroresRecientes(@Param("idEstudiante") Long idEstudiante,
                                             @Param("idTema") Long idTema,
                                             Pageable pageable);
}
