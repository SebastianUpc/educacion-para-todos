package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Progreso;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface IProgresoRepository extends JpaRepository<Progreso, Long> {

    List<Progreso> findByEstudianteId(Long estudianteId);

    boolean existsByTemaId(Long temaId);

    Optional<Progreso> findFirstByEstudianteIdAndTemaIdOrderByUpdatedAtDesc(Long estudianteId, Long temaId);

    // Dashboard: avance promedio de un grupo de estudiantes
    @Query("select avg(p.porcentajeAvance) from Progreso p where p.estudiante.id in :ids")
    BigDecimal promedioAvance(@Param("ids") Collection<Long> ids);

    // Dashboard: avance promedio por estudiante. Fila: [idEstudiante, promedio]
    @Query("select p.estudiante.id, avg(p.porcentajeAvance) from Progreso p" +
            " where p.estudiante.id in :ids group by p.estudiante.id")
    List<Object[]> promedioAvancePorEstudiante(@Param("ids") Collection<Long> ids);
}
