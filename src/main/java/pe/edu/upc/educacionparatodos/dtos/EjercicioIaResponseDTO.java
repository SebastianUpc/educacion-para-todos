package pe.edu.upc.educacionparatodos.dtos;

import java.util.List;

// Respuesta de la IA para que el docente la revise antes de guardar (HU-105, CA06).
// Los ejercicios aun NO estan en la base de datos: cada uno ya viene con el
// formato de EjercicioInsertDTO para poder enviarlo tal cual a /guardar.
public class EjercicioIaResponseDTO {

    private Long idEstudiante;
    private String nombreEstudiante;
    private Long idTema;
    private String nombreTema;
    private Integer nivelAplicado;

    // Resumen de lo que se le dijo a la IA sobre el estudiante (CA03): se muestra
    // al docente para que sepa por que salieron esos ejercicios.
    private List<String> contextoUsado;

    private List<EjercicioInsertDTO> ejercicios;

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public Long getIdTema() {
        return idTema;
    }

    public void setIdTema(Long idTema) {
        this.idTema = idTema;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
    }

    public Integer getNivelAplicado() {
        return nivelAplicado;
    }

    public void setNivelAplicado(Integer nivelAplicado) {
        this.nivelAplicado = nivelAplicado;
    }

    public List<String> getContextoUsado() {
        return contextoUsado;
    }

    public void setContextoUsado(List<String> contextoUsado) {
        this.contextoUsado = contextoUsado;
    }

    public List<EjercicioInsertDTO> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioInsertDTO> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
