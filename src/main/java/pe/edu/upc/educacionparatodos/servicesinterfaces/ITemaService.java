package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Tema;

import java.util.List;
import java.util.Optional;

public interface ITemaService {

    void insert(Tema u);

    List<Tema> list();

    Optional<Tema> listId(Long id);

    void update(Tema u);

    void delete(Long id);
}
