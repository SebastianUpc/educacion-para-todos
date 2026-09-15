package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Rol;

import java.util.List;
import java.util.Optional;

public interface IRolService {

    void insert(Rol u);

    List<Rol> list();

    Optional<Rol> listId(Long id);

    void update(Rol u);

    void delete(Long id);

}
