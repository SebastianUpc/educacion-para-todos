package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IDetalleEvaluacionService;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleEvaluacionServiceImplement implements IDetalleEvaluacionService {

    private final IDetalleEvaluacionRepository dR;

    public DetalleEvaluacionServiceImplement(IDetalleEvaluacionRepository dR) {
        this.dR = dR;
    }

    @Override
    public void insert(DetalleEvaluacion u) {
        dR.save(u);
    }

    @Override
    public List<DetalleEvaluacion> list() {
        return dR.findAll();
    }

    @Override
    public Optional<DetalleEvaluacion> listId(Long id) {
        return dR.findById(id);
    }

    @Override
    public void update(DetalleEvaluacion u) {
        dR.save(u);
    }

    @Override
    public void delete(Long id) {
        dR.deleteById(id);
    }
}
