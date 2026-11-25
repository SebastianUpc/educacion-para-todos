package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Progreso;

import java.util.List;
import java.util.Optional;

public interface IProgresoService {

    void insert(Progreso x);

    List<Progreso> list();

    Optional<Progreso> listId(Long id);

    void delete(Long id);
}
