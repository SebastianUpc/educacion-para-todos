package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Usuario> findByRolId(Long rolId);

    boolean existsByAulaId(Long aulaId);

    boolean existsByRolId(Long rolId);

    // Query simple: lista los usuarios filtrados por su estado (ACTIVO / INACTIVO)
    List<Usuario> findByEstado(String estado);
}
