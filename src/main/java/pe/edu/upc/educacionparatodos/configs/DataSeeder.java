package pe.edu.upc.educacionparatodos.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.repositories.IRolRepository;
import pe.edu.upc.educacionparatodos.repositories.IUsuarioRepository;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedAdminUser(IRolRepository rolRepository,
                                           IUsuarioRepository usuarioRepository,
                                           PasswordEncoder passwordEncoder) {
        return args -> seedIfMissing(rolRepository, usuarioRepository, passwordEncoder);
    }

    @Transactional
    public void seedIfMissing(IRolRepository rolRepository,
                              IUsuarioRepository usuarioRepository,
                              PasswordEncoder passwordEncoder) {

        Rol adminRole = rolRepository.findAll().stream()
                .filter(rol -> "ADMIN".equalsIgnoreCase(rol.getNombre()))
                .findFirst()
                .orElseGet(() -> {
                    Rol role = new Rol();
                    role.setNombre("ADMIN");
                    role.setDescripcionRol("Administrador del sistema");
                    return rolRepository.save(role);
                });

        if (usuarioRepository.findByEmail("admin@educacion.edu.pe").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellido("Sistema");
            admin.setEmail("admin@educacion.edu.pe");
            admin.setPasswordHash(passwordEncoder.encode("Admin123!"));
            admin.setRol(adminRole);
            admin.setEstado("ACTIVO");
            usuarioRepository.save(admin);
        }
    }
}
