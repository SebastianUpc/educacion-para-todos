package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.repositories.IEjercicioRepository;
import pe.edu.upc.educacionparatodos.repositories.ITemaRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;

import java.util.List;

@Service
public class TemaServiceImplement implements ITemaService {

    private final ITemaRepository temaRepository;
    private final IEjercicioRepository ejercicioRepository;

    public TemaServiceImplement(ITemaRepository temaRepository, IEjercicioRepository ejercicioRepository) {
        this.temaRepository = temaRepository;
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    public List<Tema> list() {
        return temaRepository.findAll();
    }

    @Override
    public Tema findById(Long id) {
        return temaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tema con el id: " + id
                ));
    }

    @Override
    public Tema insert(Tema t) {
        return temaRepository.save(t);
    }

    @Override
    public Tema update(Long id, Tema t) {
        Tema existente = findById(id);
        existente.setMateria(t.getMateria());
        existente.setNombre(t.getNombre());
        return temaRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        Tema tema = findById(id);

        if (ejercicioRepository.existsByTemaId(tema.getId())) {
            throw new BusinessRuleException(
                    "No se puede eliminar el tema porque tiene ejercicios asociados"
            );
        }

        temaRepository.deleteById(tema.getId());
    }
}
