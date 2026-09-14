package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Progreso;

import java.util.List;

@Repository
public interface IProgresoRepository extends JpaRepository<Progreso, Long> {

    List<Progreso> findByEstudianteId(Long estudianteId);

    boolean existsByEstudianteIdAndTemaId(Long estudianteId, Long temaId);
}
