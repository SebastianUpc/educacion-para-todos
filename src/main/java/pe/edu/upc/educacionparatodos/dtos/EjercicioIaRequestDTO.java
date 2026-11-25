package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Parametros que el docente elige para pedir ejercicios a la IA (HU-105, CA02)
public class EjercicioIaRequestDTO {

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "El tema es obligatorio")
    private Long idTema;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "Se puede pedir entre 1 y 5 ejercicios")
    @Max(value = 5, message = "Se puede pedir entre 1 y 5 ejercicios")
    private Integer cantidad;

    // Opcional: si no se envia, se usa el nivel del progreso del estudiante en el tema
    @Min(value = 1, message = "La dificultad debe estar entre 1 y 5")
    @Max(value = 5, message = "La dificultad debe estar entre 1 y 5")
    private Integer nivelDificultad;

    // Opcional: lo que el docente observa del estudiante ("confunde numerador y denominador")
    @Size(max = 500, message = "Las necesidades de aprendizaje no pueden superar los 500 caracteres")
    private String necesidades;

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public Long getIdTema() {
        return idTema;
    }

    public void setIdTema(Long idTema) {
        this.idTema = idTema;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(Integer nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public String getNecesidades() {
        return necesidades;
    }

    public void setNecesidades(String necesidades) {
        this.necesidades = necesidades;
    }
}
