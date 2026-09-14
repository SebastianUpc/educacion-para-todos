package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Progreso;
import pe.edu.upc.educacionparatodos.repositories.IProgresoRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IProgresoService;

import java.util.List;
import java.util.Optional;

@Service
public class ProgresoServiceImplement implements IProgresoService {

    private final IProgresoRepository progresoRepository;

    public ProgresoServiceImplement(IProgresoRepository progresoRepository) {
        this.progresoRepository = progresoRepository;
    }

    @Override
    public void insert(Progreso u) {
        progresoRepository.save(u);
    }

    @Override
    public List<Progreso> list() {
        return progresoRepository.findAll();
    }

    @Override
    public Optional<Progreso> listId(Long id) {
        return progresoRepository.findById(id);
    }

    @Override
    public void update(Progreso u) {
        progresoRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        progresoRepository.deleteById(id);
    }
}
