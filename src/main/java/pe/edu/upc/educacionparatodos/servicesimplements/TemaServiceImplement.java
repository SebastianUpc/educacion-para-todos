package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.repositories.ITemaRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;

import java.util.List;
import java.util.Optional;

@Service
public class TemaServiceImplement implements ITemaService {

    private final ITemaRepository temaRepository;

    public TemaServiceImplement(ITemaRepository temaRepository) {
        this.temaRepository = temaRepository;
    }

    @Override
    public void insert(Tema u) {
        temaRepository.save(u);
    }

    @Override
    public List<Tema> list() {
        return temaRepository.findAll();
    }

    @Override
    public Optional<Tema> listId(Long id) {
        return temaRepository.findById(id);
    }

    @Override
    public void update(Tema u) {
        temaRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        temaRepository.deleteById(id);
    }
}
