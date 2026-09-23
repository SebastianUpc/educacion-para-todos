package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Colegio;
import pe.edu.upc.educacionparatodos.repositories.IColegioRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IColegioService;

import java.util.List;
import java.util.Optional;

@Service
public class ColegioServiceImplement implements IColegioService {

    private final IColegioRepository cR;

    public ColegioServiceImplement(IColegioRepository cR) {
        this.cR = cR;
    }

    @Override
    public void insert(Colegio u) {
        cR.save(u);
    }

    @Override
    public List<Colegio> list() {
        return cR.findAll();
    }

    @Override
    public Optional<Colegio> listId(Long id) {
        return cR.findById(id);
    }

    @Override
    public void update(Colegio u) {
        cR.save(u);
    }

    @Override
    public void delete(Long id) {
        cR.deleteById(id);
    }

    @Override
    public List<Object[]> contarColegiosPorDistrito() {
        return cR.contarColegiosPorDistrito();
    }
}
