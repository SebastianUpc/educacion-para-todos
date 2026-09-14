package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Rol;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Long> {

    boolean existsByNombre(String nombre);
}
