package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Tema;

import java.util.List;

@Repository
public interface ITemaRepository extends JpaRepository<Tema, Long> {

    List<Tema> findByMateria(String materia);

    boolean existsByMateriaAndNombre(String materia, String nombre);
}
