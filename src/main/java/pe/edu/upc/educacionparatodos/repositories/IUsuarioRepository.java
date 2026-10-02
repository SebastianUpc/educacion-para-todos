package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    // Query con JOIN: cuenta cuantos usuarios hay registrados en cada rol.
    // Se usa left join desde roles para que los roles sin usuarios tambien salgan, con cantidad 0.
    @Query(value = "select r.nombre_rol, count(u.id)\n" +
            " from roles r left join usuarios u\n" +
            " on r.id = u.rol_id\n" +
            " group by r.nombre_rol\n" +
            " order by count(u.id) desc", nativeQuery = true)
    List<Object[]> contarUsuariosPorRol();
}
