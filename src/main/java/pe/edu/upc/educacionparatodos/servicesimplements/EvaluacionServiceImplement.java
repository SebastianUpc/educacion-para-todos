package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Evaluacion;
import pe.edu.upc.educacionparatodos.repositories.IEvaluacionRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEvaluacionService;

import java.util.List;
import java.util.Optional;

@Service
public class EvaluacionServiceImplement implements IEvaluacionService {

    private final IEvaluacionRepository evaluacionRepository;

    public EvaluacionServiceImplement(IEvaluacionRepository evaluacionRepository) {
        this.evaluacionRepository = evaluacionRepository;
    }

    @Override
    public void insert(Evaluacion u) {
        evaluacionRepository.save(u);
    }

    @Override
    public List<Evaluacion> list() {
        return evaluacionRepository.findAll();
    }

    @Override
    public Optional<Evaluacion> listId(Long id) {
        return evaluacionRepository.findById(id);
    }

    @Override
    public void update(Evaluacion u) {
        evaluacionRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        evaluacionRepository.deleteById(id);
    }

    @Override
    public List<Evaluacion> listarPorEstado(String estado) {
        return evaluacionRepository.findByEstado(estado);
    }

    @Override
    public List<Object[]> contarEvaluacionesPorEstudiante() {
        return evaluacionRepository.contarEvaluacionesPorEstudiante();
    }
}
