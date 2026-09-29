package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Ejercicio;

import java.util.List;

public interface IEjercicioService {

    List<Ejercicio> list();

    Ejercicio findById(Long id);

    // El ejercicio debe traer un Tema con al menos su id; el servicio valida que exista
    Ejercicio insert(Ejercicio e);

    Ejercicio update(Long id, Ejercicio e);

    void delete(Long id);
}
