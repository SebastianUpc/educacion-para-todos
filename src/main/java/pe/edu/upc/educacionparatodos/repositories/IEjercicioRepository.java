package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;

import java.util.List;

@Repository
public interface IEjercicioRepository extends JpaRepository<Ejercicio, Long> {

    List<Ejercicio> findByTemaId(Long temaId);

    boolean existsByTemaId(Long temaId);
}
