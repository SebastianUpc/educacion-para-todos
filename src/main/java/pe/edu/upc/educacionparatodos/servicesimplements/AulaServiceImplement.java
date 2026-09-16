package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.repositories.IAulaRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;

import java.util.List;
import java.util.Optional;

@Service
public class AulaServiceImplement implements IAulaService {

    private final IAulaRepository aR;

    public AulaServiceImplement(IAulaRepository aR)
    {
        this.aR = aR;
    }

    @Override
    public void insert(Aula u) {
        aR.save(u);
    }

    @Override
    public List<Aula> list() {
        return aR.findAll();
    }

    @Override
    public Optional<Aula> listId(Long id) {
        return aR.findById(id);
    }

    @Override
    public void update(Aula u) {
        aR.save(u);
    }

    @Override
    public void delete(Long id) {
        aR.deleteById(id);
    }
}
