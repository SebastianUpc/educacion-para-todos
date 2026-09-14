package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;

import java.util.List;
import java.util.Optional;

public interface IDetalleEvaluacionService {

    void insert(DetalleEvaluacion u);

    List<DetalleEvaluacion> list();

    Optional<DetalleEvaluacion> listId(Long id);

    void update(DetalleEvaluacion u);

    void delete(Long id);
}
