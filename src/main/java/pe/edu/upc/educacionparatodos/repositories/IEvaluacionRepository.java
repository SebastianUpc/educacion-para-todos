package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Evaluacion;

import java.util.Collection;
import java.util.List;

@Repository
public interface IEvaluacionRepository extends JpaRepository<Evaluacion, Long> {

    List<Evaluacion> findByEstudianteIdOrderByFechaInicioDesc(Long estudianteId);

    // Query simple: lista las evaluaciones filtradas por su estado (EN_CURSO / FINALIZADA)
    List<Evaluacion> findByEstado(String estado);

    // Dashboard: evaluaciones de un grupo de estudiantes (todas y por estado)
    long countByEstudianteIdIn(Collection<Long> ids);

    long countByEstudianteIdInAndEstado(Collection<Long> ids, String estado);

    // Query con JOIN: cuenta cuantas evaluaciones tiene rendidas cada estudiante.
    // Se agrupa por u.id y no por el nombre: el nombre no es unico, asi que agrupar
    // por el fusionaria en una sola fila a dos estudiantes que se llamen igual.
    // Se muestra nombre y apellido para poder distinguirlos.
    @Query(value = "select concat(u.nombre, ' ', u.apellido), count(e.id)\n" +
            " from usuarios u left join evaluaciones e\n" +
            " on u.id = e.estudiante_id\n" +
            " group by u.id, u.nombre, u.apellido\n" +
            " order by count(e.id) desc", nativeQuery = true)
    List<Object[]> contarEvaluacionesPorEstudiante();
}
