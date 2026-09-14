package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;

import java.util.List;

@Repository
public interface IDetalleEvaluacionRepository extends JpaRepository<DetalleEvaluacion, Long> {

    List<DetalleEvaluacion> findByEvaluacionId(Long evaluacionId);

    boolean existsByEvaluacionId(Long evaluacionId);
}
