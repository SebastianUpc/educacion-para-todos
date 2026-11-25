package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.repositories.IUsuarioRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;

import java.util.List;

@Service
public class AlcanceAcademicoServiceImplement implements IAlcanceAcademicoService {

    private static final String ROL_ESTUDIANTE = "ESTUDIANTE";
    private static final String ESTADO_ACTIVO = "ACTIVO";

    private final IUsuarioRepository usuarioRepository;

    public AlcanceAcademicoServiceImplement(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // El subject del JWT es el email del usuario (ver JwtTokenService)
    @Override
    public Usuario usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new AccessDeniedException("No hay una sesión activa");
        }
        return usuarioRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("El usuario de la sesión ya no existe"));
    }

    @Override
    public List<Usuario> estudiantesVisibles() {
        Usuario actual = usuarioActual();

        if (esAdmin()) {
            return usuarioRepository.findByRolNombreIgnoreCaseAndEstado(ROL_ESTUDIANTE, ESTADO_ACTIVO);
        }

        // Un docente sin aula asignada no tiene estudiantes a su cargo
        if (actual.getAula() == null) {
            return List.of();
        }

        return usuarioRepository.findByAulaIdAndRolNombreIgnoreCaseAndEstado(
                actual.getAula().getId(), ROL_ESTUDIANTE, ESTADO_ACTIVO);
    }

    @Override
    public Usuario validarEstudianteVisible(Long idEstudiante) {
        Usuario estudiante = usuarioRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estudiante con el id: " + idEstudiante));

        boolean visible = estudiantesVisibles().stream()
                .anyMatch(e -> e.getId().equals(estudiante.getId()));

        if (!visible) {
            throw new AccessDeniedException(
                    "El estudiante no pertenece a sus aulas asignadas o no está activo");
        }
        return estudiante;
    }

    private boolean esAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));
    }
}
