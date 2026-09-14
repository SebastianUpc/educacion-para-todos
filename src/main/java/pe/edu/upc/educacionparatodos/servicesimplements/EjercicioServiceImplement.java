package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.repositories.IEjercicioRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;

import java.util.List;
import java.util.Optional;

@Service
public class EjercicioServiceImplement implements IEjercicioService {

    private final IEjercicioRepository ejercicioRepository;

    public EjercicioServiceImplement(IEjercicioRepository ejercicioRepository) {
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    public void insert(Ejercicio u) {
        ejercicioRepository.save(u);
    }

    @Override
    public List<Ejercicio> list() {
        return ejercicioRepository.findAll();
    }

    @Override
    public Optional<Ejercicio> listId(Long id) {
        return ejercicioRepository.findById(id);
    }

    @Override
    public void update(Ejercicio u) {
        ejercicioRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        ejercicioRepository.deleteById(id);
    }
}
