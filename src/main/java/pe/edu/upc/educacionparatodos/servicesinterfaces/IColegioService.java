package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Colegio;

import java.util.List;
import java.util.Optional;

public interface IColegioService {

    void insert(Colegio u);

    List<Colegio> list();

    Optional<Colegio> listId(Long id);

    void update(Colegio u);

    void delete(Long id);
}
