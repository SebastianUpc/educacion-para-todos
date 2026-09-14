package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;
import pe.edu.upc.educacionparatodos.repositories.IDetalleEvaluacionRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IDetalleEvaluacionService;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleEvaluacionServiceImplement implements IDetalleEvaluacionService {

    private final IDetalleEvaluacionRepository detalleEvaluacionRepository;

    public DetalleEvaluacionServiceImplement(IDetalleEvaluacionRepository detalleEvaluacionRepository) {
        this.detalleEvaluacionRepository = detalleEvaluacionRepository;
    }

    @Override
    public void insert(DetalleEvaluacion u) {
        detalleEvaluacionRepository.save(u);
    }

    @Override
    public List<DetalleEvaluacion> list() {
        return detalleEvaluacionRepository.findAll();
    }

    @Override
    public Optional<DetalleEvaluacion> listId(Long id) {
        return detalleEvaluacionRepository.findById(id);
    }

    @Override
    public void update(DetalleEvaluacion u) {
        detalleEvaluacionRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        detalleEvaluacionRepository.deleteById(id);
    }
}
