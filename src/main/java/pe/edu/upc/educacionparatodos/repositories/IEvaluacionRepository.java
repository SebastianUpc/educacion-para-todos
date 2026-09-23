package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Evaluacion;

import java.util.List;

@Repository
public interface IEvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByEstudianteIdOrderByFechaInicioDesc(Long estudianteId);

    // Query simple: lista las evaluaciones filtradas por su estado (EN_CURSO / FINALIZADA)
    List<Evaluacion> findByEstado(String estado);

    // Query con JOIN: cuenta cuantas evaluaciones tiene rendidas cada estudiante
    @Query(value = "select u.nombre, count(e.id)\n" +
            " from usuarios u left join evaluaciones e\n" +
            " on u.id = e.estudiante_id\n" +
            " group by u.nombre", nativeQuery = true)
    List<Object[]> contarEvaluacionesPorEstudiante();
}
