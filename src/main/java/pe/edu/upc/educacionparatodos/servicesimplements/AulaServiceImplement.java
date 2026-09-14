package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.repositories.IAulaRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;

import java.util.List;
import java.util.Optional;

@Service
public class AulaServiceImplement implements IAulaService {

    private final IAulaRepository aulaRepository;

    public AulaServiceImplement(IAulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @Override
    public void insert(Aula u) {
        aulaRepository.save(u);
    }

    @Override
    public List<Aula> list() {
        return aulaRepository.findAll();
    }

    @Override
    public Optional<Aula> listId(Long id) {
        return aulaRepository.findById(id);
    }

    @Override
    public void update(Aula u) {
        aulaRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        aulaRepository.deleteById(id);
    }
}
