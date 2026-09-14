package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Ejercicio;

import java.util.List;
import java.util.Optional;

public interface IEjercicioService {

    void insert(Ejercicio u);

    List<Ejercicio> list();

    Optional<Ejercicio> listId(Long id);

    void update(Ejercicio u);

    void delete(Long id);
}
