package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Usuario;

import java.util.List;

// Decide que estudiantes puede ver el usuario que inicio sesion.
// ADMIN ve a todos; DOCENTE solo a los estudiantes de su aula (HU-106 CA02).
public interface IAlcanceAcademicoService {

    Usuario usuarioActual();

    List<Usuario> estudiantesVisibles();

    // Lanza 403 si el estudiante no esta dentro del alcance del usuario actual
    Usuario validarEstudianteVisible(Long idEstudiante);
}
