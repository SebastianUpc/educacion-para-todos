package pe.edu.upc.educacionparatodos.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.repositories.IUsuarioRepository;

import java.util.List;
import java.util.Locale;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository usuarioRepository;

    public CustomUserDetailsService(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        String roleName = usuario.getRol() != null && usuario.getRol().getNombre() != null
                ? usuario.getRol().getNombre().trim()
                : "ROLE_USER";

        // hasRole('ADMIN') busca la autoridad ROLE_ADMIN, asi que el prefijo es obligatorio.
        // Se comprueba antes para no generar ROLE_ROLE_ADMIN si el rol ya viene prefijado.
        String upperRole = roleName.toUpperCase(Locale.ROOT);
        String normalizedRole = upperRole.startsWith("ROLE_") ? upperRole : "ROLE_" + upperRole;

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority(normalizedRole)))
                .accountLocked(false)
                .accountExpired(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
