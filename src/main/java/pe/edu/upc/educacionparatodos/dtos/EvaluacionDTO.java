package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EvaluacionDTO {

    private Long id;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;

    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;

    public EvaluacionDTO() {
    }

    public EvaluacionDTO(Long id, Long idEstudiante, String estado,
                         LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.estado = estado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
