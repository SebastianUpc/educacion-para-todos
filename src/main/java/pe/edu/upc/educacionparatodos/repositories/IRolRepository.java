package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Rol;

import java.util.Optional;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByIdRol(Long idRol);
    boolean existsByNombre(String nombre);
}
