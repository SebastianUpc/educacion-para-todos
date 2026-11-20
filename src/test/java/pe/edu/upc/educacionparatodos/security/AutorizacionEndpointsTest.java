package pe.edu.upc.educacionparatodos.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.security.access.prepost.PreAuthorize;
import pe.edu.upc.educacionparatodos.controllers.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Fija la matriz de autorizacion de la API.
 *
 * Cada caso comprueba que un metodo de controlador siga declarando exactamente
 * los roles esperados en su @PreAuthorize. Si alguien amplia o restringe un
 * permiso sin querer, esta prueba falla y lo deja a la vista.
 *
 * No levanta el contexto de Spring: lee las anotaciones por reflexion, asi que
 * se ejecuta sin base de datos.
 */
class AutorizacionEndpointsTest {

    private static final Pattern ROL = Pattern.compile("'(\\w+)'");

    private static Arguments caso(Class<?> controlador, String metodo, String... roles) {
        return Arguments.of(controlador, metodo, new LinkedHashSet<>(Arrays.asList(roles)));
    }

    static Stream<Arguments> matrizDeAutorizacion() {
        return Stream.of(
                // --- Usuarios: DOCENTE necesita listar para alimentar los desplegables
                caso(UsuarioController.class, "listar", "ADMIN", "DOCENTE"),
                caso(UsuarioController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(UsuarioController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(UsuarioController.class, "eliminar", "ADMIN"),
                caso(UsuarioController.class, "buscarPorEstado", "ADMIN", "DOCENTE"),
                caso(UsuarioController.class, "contarPorRol", "ADMIN", "DOCENTE"),

                // --- Roles: listar lo necesita DOCENTE; el resto es exclusivo de ADMIN
                caso(RolController.class, "listar", "ADMIN", "DOCENTE"),
                caso(RolController.class, "buscarId", "ADMIN"),
                caso(RolController.class, "registrar", "ADMIN"),
                caso(RolController.class, "actualizar", "ADMIN"),
                caso(RolController.class, "eliminar", "ADMIN"),

                // --- Colegios: la escritura es solo de ADMIN
                caso(ColegioController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(ColegioController.class, "registrar", "ADMIN"),
                caso(ColegioController.class, "actualizar", "ADMIN"),
                caso(ColegioController.class, "eliminar", "ADMIN"),
                caso(ColegioController.class, "contarPorDistrito", "ADMIN", "DOCENTE"),

                // --- Aulas: DOCENTE puede crear y editar, pero no eliminar
                caso(AulaController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(AulaController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(AulaController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(AulaController.class, "eliminar", "ADMIN"),
                caso(AulaController.class, "contarPorColegio", "ADMIN", "DOCENTE"),

                // --- Temas y Ejercicios: mismo criterio que Aulas
                caso(TemaController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(TemaController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(TemaController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(TemaController.class, "eliminar", "ADMIN"),
                caso(EjercicioController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EjercicioController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(EjercicioController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(EjercicioController.class, "eliminar", "ADMIN"),

                // --- Evaluaciones: el historial lo consulta cualquier perfil autenticado
                caso(EvaluacionController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EvaluacionController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(EvaluacionController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(EvaluacionController.class, "eliminar", "ADMIN", "DOCENTE"),
                caso(EvaluacionController.class, "buscarPorEstado", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EvaluacionController.class, "listarPorEstudiante", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EvaluacionController.class, "contarPorEstudiante", "ADMIN", "DOCENTE"),

                // --- Recursos de solo lectura
                caso(ProgresoController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(DetalleEvaluacionController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),

                // --- Consulta por id: abierta a cualquier perfil autenticado en todos los recursos
                caso(ColegioController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(AulaController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(TemaController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EjercicioController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(UsuarioController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(EvaluacionController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(ProgresoController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(DetalleEvaluacionController.class, "buscarId", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO")
        );
    }

    @ParameterizedTest(name = "{0}.{1} debe permitir {2}")
    @MethodSource("matrizDeAutorizacion")
    @DisplayName("Cada endpoint declara exactamente los roles esperados")
    void elEndpointDeclaraLosRolesEsperados(Class<?> controlador, String metodo, Set<String> esperados) {
        Method encontrado = Arrays.stream(controlador.getDeclaredMethods())
                .filter(m -> m.getName().equals(metodo))
                .findFirst()
                .orElse(null);

        assertThat(encontrado)
                .as("El metodo %s no existe en %s", metodo, controlador.getSimpleName())
                .isNotNull();

        PreAuthorize anotacion = encontrado.getAnnotation(PreAuthorize.class);
        assertThat(anotacion)
                .as("%s.%s no tiene @PreAuthorize: quedaria abierto a cualquier usuario autenticado",
                        controlador.getSimpleName(), metodo)
                .isNotNull();

        assertThat(rolesDe(anotacion.value()))
                .as("Los roles de %s.%s no son los esperados", controlador.getSimpleName(), metodo)
                .isEqualTo(esperados);
    }

    private Set<String> rolesDe(String expresion) {
        Set<String> roles = new LinkedHashSet<>();
        Matcher m = ROL.matcher(expresion);
        while (m.find()) {
            roles.add(m.group(1));
        }
        return roles;
    }
}
