package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Aula;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long> {

    Optional<Aula> findByIdAula(Long idAula);
    public boolean existsByColegioId(Long colegioId);
}
