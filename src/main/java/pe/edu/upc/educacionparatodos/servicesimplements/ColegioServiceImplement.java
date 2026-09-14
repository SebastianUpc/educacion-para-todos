package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Colegio;
import pe.edu.upc.educacionparatodos.repositories.IColegioRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IColegioService;

import java.util.List;
import java.util.Optional;

@Service
public class ColegioServiceImplement implements IColegioService {

    private final IColegioRepository colegioRepository;

    public ColegioServiceImplement(IColegioRepository colegioRepository) {
        this.colegioRepository = colegioRepository;
    }

    @Override
    public void insert(Colegio u) {
        colegioRepository.save(u);
    }

    @Override
    public List<Colegio> list() {
        return colegioRepository.findAll();
    }

    @Override
    public Optional<Colegio> listId(Long id) {
        return colegioRepository.findById(id);
    }

    @Override
    public void update(Colegio u) {
        colegioRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        colegioRepository.deleteById(id);
    }
}
