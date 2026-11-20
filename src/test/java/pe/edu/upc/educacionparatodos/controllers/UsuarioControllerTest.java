package pe.edu.upc.educacionparatodos.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import pe.edu.upc.educacionparatodos.dtos.UsuarioDTO;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IRolService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas de las reglas de negocio de UsuarioController.
 *
 * Son pruebas unitarias: el controlador se construye a mano con dobles de prueba
 * de los servicios, sin levantar el contexto de Spring ni la base de datos. Eso
 * las hace rapidas y permite ejecutarlas sin PostgreSQL disponible.
 */
class UsuarioControllerTest {

    private IUsuarioService usuarioService;
    private IRolService rolService;
    private IAulaService aulaService;
    private PasswordEncoder passwordEncoder;
    private UsuarioController controller;

    @BeforeEach
    void prepararControlador() {
        usuarioService = mock(IUsuarioService.class);
        rolService = mock(IRolService.class);
        aulaService = mock(IAulaService.class);
        passwordEncoder = mock(PasswordEncoder.class);

        controller = new UsuarioController(
                usuarioService, rolService, aulaService, new ModelMapper(), passwordEncoder);
    }

    private Usuario usuarioDePrueba(Long id, String nombre, String estado) {
        Rol rol = new Rol();
        rol.setId(3L);
        rol.setNombre("ESTUDIANTE");

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setApellido("Quispe");
        usuario.setEmail(nombre.toLowerCase() + "@colegio.pe");
        usuario.setPasswordHash("$2a$10$hashDePrueba");
        usuario.setEstado(estado);
        usuario.setRol(rol);
        return usuario;
    }

    @Test
    @DisplayName("El listado excluye a los usuarios dados de baja logicamente")
    void listarExcluyeInactivos() {
        when(usuarioService.list()).thenReturn(List.of(
                usuarioDePrueba(1L, "Ana", "ACTIVO"),
                usuarioDePrueba(2L, "Pedro", "INACTIVO"),
                usuarioDePrueba(3L, "Luis", "ACTIVO")
        ));

        ResponseEntity<List<UsuarioDTO>> respuesta = controller.listar();
        List<UsuarioDTO> lista = respuesta.getBody();

        assertThat(lista).isNotNull();
        assertThat(lista).hasSize(2);
        assertThat(lista).extracting(UsuarioDTO::getNombre).containsExactly("Ana", "Luis");
        assertThat(lista).noneMatch(u -> "INACTIVO".equals(u.getEstado()));
    }

    @Test
    @DisplayName("El listado nunca devuelve la contrasena")
    void listarNoExponeLaContrasena() {
        when(usuarioService.list()).thenReturn(List.of(usuarioDePrueba(1L, "Ana", "ACTIVO")));

        List<UsuarioDTO> lista = controller.listar().getBody();

        assertThat(lista).isNotNull().hasSize(1);
        assertThat(lista.get(0).getPassword()).isNull();
    }

    @Test
    @DisplayName("El listado expone el id del rol, no la entidad completa")
    void listarExponeElIdDelRol() {
        when(usuarioService.list()).thenReturn(List.of(usuarioDePrueba(1L, "Ana", "ACTIVO")));

        List<UsuarioDTO> lista = controller.listar().getBody();

        assertThat(lista).isNotNull().hasSize(1);
        assertThat(lista.get(0).getIdRol()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Registrar sin contrasena es un error de regla de negocio")
    void registrarSinContrasenaFalla() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Carmen");
        dto.setApellido("Ccopa");
        dto.setEmail("carmen@colegio.pe");
        dto.setPassword(null);
        dto.setIdRol(3L);

        assertThatThrownBy(() -> controller.registrar(dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("contraseña");
    }

    @Test
    @DisplayName("Registrar con la contrasena en blanco tambien falla")
    void registrarConContrasenaEnBlancoFalla() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("carmen@colegio.pe");
        dto.setPassword("   ");
        dto.setIdRol(3L);

        assertThatThrownBy(() -> controller.registrar(dto))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("No se permite registrar dos usuarios con el mismo correo")
    void registrarConCorreoDuplicadoFalla() {
        when(usuarioService.existsByEmail(anyString())).thenReturn(true);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("ana@colegio.pe");
        dto.setPassword("Clave123!");
        dto.setIdRol(3L);

        assertThatThrownBy(() -> controller.registrar(dto))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("ana@colegio.pe");
    }
}
