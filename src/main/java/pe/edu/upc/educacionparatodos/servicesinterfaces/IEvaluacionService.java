package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Evaluacion;

import java.util.List;
import java.util.Optional;

public interface IEvaluacionService {

    void insert(Evaluacion u);

    List<Evaluacion> list();

    Optional<Evaluacion> listId(Long id);

    void update(Evaluacion u);

    void delete(Long id);

    List<Object[]> contarEvaluacionesPorEstudiante();
}
