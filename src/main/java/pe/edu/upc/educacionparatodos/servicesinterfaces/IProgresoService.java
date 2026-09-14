package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Progreso;

import java.util.List;
import java.util.Optional;

public interface IProgresoService {

    void insert(Progreso u);

    List<Progreso> list();

    Optional<Progreso> listId(Long id);

    void update(Progreso u);

    void delete(Long id);
}
