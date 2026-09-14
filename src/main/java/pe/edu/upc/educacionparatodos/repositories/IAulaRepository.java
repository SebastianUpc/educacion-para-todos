package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Aula;

import java.util.List;

@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long> {

    List<Aula> findByColegioId(Long colegioId);

    boolean existsByColegioId(Long colegioId);
}
