package pe.edu.upc.educacionparatodos.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import pe.edu.upc.educacionparatodos.dtos.LoginRequestDTO;
import pe.edu.upc.educacionparatodos.dtos.LoginResponseDTO;
import pe.edu.upc.educacionparatodos.security.JwtTokenService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Pruebas del inicio de sesion.
 *
 * Lo que se comprueba es el contrato de la respuesta: el frontend necesita el
 * token, el usuario y los roles para decidir que opciones muestra y a que rutas
 * deja entrar. Si alguno de esos tres falta, el roleGuard deja de funcionar.
 */
class LoginControllerTest {

    private AuthenticationManager authenticationManager;
    private JwtTokenService jwtTokenService;
    private LoginController controller;

    @BeforeEach
    void prepararControlador() {
        authenticationManager = mock(AuthenticationManager.class);
        jwtTokenService = mock(JwtTokenService.class);
        controller = new LoginController(authenticationManager, jwtTokenService);
    }

    private void autenticarComo(String usuario, String... autoridades) {
        UserDetails userDetails = User.withUsername(usuario)
                .password("irrelevante")
                .authorities(java.util.Arrays.stream(autoridades)
                        .map(SimpleGrantedAuthority::new)
                        .toList())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenService.generateToken(any())).thenReturn("token-de-prueba");
    }

    private LoginRequestDTO credenciales(String usuario) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername(usuario);
        dto.setPassword("Clave123!");
        return dto;
    }

    @Test
    @DisplayName("La respuesta del login devuelve el token y el usuario")
    void devuelveTokenYUsuario() {
        autenticarComo("admin@educacion.pe", "ROLE_ADMIN");

        ResponseEntity<LoginResponseDTO> respuesta =
                controller.login(credenciales("admin@educacion.pe"));
        LoginResponseDTO cuerpo = respuesta.getBody();

        assertThat(cuerpo).isNotNull();
        assertThat(cuerpo.getToken()).isEqualTo("token-de-prueba");
        assertThat(cuerpo.getUsername()).isEqualTo("admin@educacion.pe");
    }

    @Test
    @DisplayName("La respuesta del login incluye los roles del usuario")
    void devuelveLosRoles() {
        autenticarComo("docente@educacion.pe", "ROLE_DOCENTE");

        LoginResponseDTO cuerpo = controller.login(credenciales("docente@educacion.pe")).getBody();

        assertThat(cuerpo).isNotNull();
        assertThat(cuerpo.getRoles()).containsExactly("ROLE_DOCENTE");
    }

    @Test
    @DisplayName("Los roles conservan el prefijo ROLE_ que espera Spring Security")
    void losRolesLlevanElPrefijo() {
        autenticarComo("admin@educacion.pe", "ROLE_ADMIN");

        LoginResponseDTO cuerpo = controller.login(credenciales("admin@educacion.pe")).getBody();

        assertThat(cuerpo).isNotNull();
        assertThat(cuerpo.getRoles()).allMatch(rol -> rol.startsWith("ROLE_"));
    }

    @Test
    @DisplayName("Un usuario con varios roles los recibe todos")
    void devuelveTodosLosRoles() {
        autenticarComo("mixto@educacion.pe", "ROLE_ADMIN", "ROLE_DOCENTE");

        LoginResponseDTO cuerpo = controller.login(credenciales("mixto@educacion.pe")).getBody();

        assertThat(cuerpo).isNotNull();
        assertThat(cuerpo.getRoles()).containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_DOCENTE");
    }

    @Test
    @DisplayName("La lista de roles nunca llega nula")
    void laListaDeRolesNuncaEsNula() {
        autenticarComo("sinroles@educacion.pe");

        LoginResponseDTO cuerpo = controller.login(credenciales("sinroles@educacion.pe")).getBody();

        assertThat(cuerpo).isNotNull();
        assertThat(cuerpo.getRoles()).isNotNull().isEmpty();
    }
}
