package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    void insert(Usuario u);

    List<Usuario> list();

    Optional<Usuario> listId(Long id);

    void update(Usuario u);

    void delete(Long id);

    boolean existsByEmail(String email);

    boolean existsByRolId(Long rolId);

    boolean existsByAulaId(Long aulaId);

    List<Usuario> listarPorEstado(String estado);

    List<Object[]> contarUsuariosPorRol();
}
