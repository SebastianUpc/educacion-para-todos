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
                // --- Usuarios: la gestion es solo de ADMIN. DOCENTE conserva el listado
                // porque alimenta los desplegables de estudiante, pero no gestiona usuarios.
                caso(UsuarioController.class, "listar", "ADMIN", "DOCENTE"),
                caso(UsuarioController.class, "registrar", "ADMIN"),
                caso(UsuarioController.class, "actualizar", "ADMIN"),
                caso(UsuarioController.class, "eliminar", "ADMIN"),
                caso(UsuarioController.class, "buscarPorEstado", "ADMIN"),
                caso(UsuarioController.class, "contarPorRol", "ADMIN"),

                // --- Roles: listar lo necesita DOCENTE; el resto es exclusivo de ADMIN
                caso(RolController.class, "listar", "ADMIN", "DOCENTE"),
                caso(RolController.class, "buscarId", "ADMIN"),
                caso(RolController.class, "registrar", "ADMIN"),
                caso(RolController.class, "actualizar", "ADMIN"),
                caso(RolController.class, "eliminar", "ADMIN"),

                // --- Colegios: la escritura es solo de ADMIN; ESTUDIANTE no ve colegios
                caso(ColegioController.class, "listar", "ADMIN", "DOCENTE", "APODERADO"),
                caso(ColegioController.class, "registrar", "ADMIN"),
                caso(ColegioController.class, "actualizar", "ADMIN"),
                caso(ColegioController.class, "eliminar", "ADMIN"),
                caso(ColegioController.class, "contarPorDistrito", "ADMIN"),

                // --- Aulas: DOCENTE puede crear y editar, pero no eliminar; ESTUDIANTE no ve aulas
                caso(AulaController.class, "listar", "ADMIN", "DOCENTE", "APODERADO"),
                caso(AulaController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(AulaController.class, "actualizar", "ADMIN", "DOCENTE"),
                caso(AulaController.class, "eliminar", "ADMIN"),
                caso(AulaController.class, "contarPorColegio", "ADMIN"),

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
                caso(EvaluacionController.class, "contarPorEstudiante", "ADMIN"),

                // --- Progreso y detalle de evaluacion: ahora se guardan en la base de datos
                caso(ProgresoController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(ProgresoController.class, "registrar", "ADMIN", "DOCENTE"),
                caso(ProgresoController.class, "eliminar", "ADMIN"),
                caso(DetalleEvaluacionController.class, "listar", "ADMIN", "DOCENTE", "ESTUDIANTE", "APODERADO"),
                caso(DetalleEvaluacionController.class, "registrar", "ADMIN", "DOCENTE", "ESTUDIANTE"),
                caso(DetalleEvaluacionController.class, "eliminar", "ADMIN"),

                // --- HU-105 ejercicios con IA y HU-106 dashboard: solo ADMIN y DOCENTE
                caso(EjercicioIaController.class, "listarEstudiantes", "ADMIN", "DOCENTE"),
                caso(EjercicioIaController.class, "generar", "ADMIN", "DOCENTE"),
                caso(EjercicioIaController.class, "guardar", "ADMIN", "DOCENTE"),
                caso(DashboardController.class, "obtener", "ADMIN", "DOCENTE"),

                // --- Consulta por id: abierta a cualquier perfil autenticado (salvo colegios y aulas)
                caso(ColegioController.class, "buscarId", "ADMIN", "DOCENTE", "APODERADO"),
                caso(AulaController.class, "buscarId", "ADMIN", "DOCENTE", "APODERADO"),
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
