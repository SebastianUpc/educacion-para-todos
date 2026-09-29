package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Tema;

import java.util.List;

public interface ITemaService {

    List<Tema> list();

    Tema findById(Long id);

    Tema insert(Tema t);

    Tema update(Long id, Tema t);

    void delete(Long id);
}
