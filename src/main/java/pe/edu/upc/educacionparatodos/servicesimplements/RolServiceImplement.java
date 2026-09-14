package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.repositories.IRolRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IRolService;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImplement implements IRolService {

    private final IRolRepository rolRepository;

    public RolServiceImplement(IRolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public void insert(Rol u) {
        rolRepository.save(u);
    }

    @Override
    public List<Rol> list() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> listId(Long id) {
        return rolRepository.findById(id);
    }

    @Override
    public void update(Rol u) {
        rolRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return rolRepository.existsByNombre(nombre);
    }
}
