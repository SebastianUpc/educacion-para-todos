package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.repositories.IUsuarioRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImplement implements IUsuarioService {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioServiceImplement(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void insert(Usuario u) {
        usuarioRepository.save(u);
    }

    @Override
    public List<Usuario> list() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> listId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public void update(Usuario u) {
        usuarioRepository.save(u);
    }

    @Override
    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByRolId(Long rolId) {
        return usuarioRepository.existsByRolId(rolId);
    }
}
