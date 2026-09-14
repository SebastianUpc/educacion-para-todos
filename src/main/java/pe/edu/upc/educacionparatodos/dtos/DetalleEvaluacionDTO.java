package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotNull;

public class DetalleEvaluacionDTO {

    private Long id;

    @NotNull(message = "La evaluación es obligatoria")
    private Long idEvaluacion;

    @NotNull(message = "El ejercicio es obligatorio")
    private Long idEjercicio;

    private String respuesta;

    private Boolean esCorrecta;

    public DetalleEvaluacionDTO() {
    }

    public DetalleEvaluacionDTO(Long id, Long idEvaluacion, Long idEjercicio, String respuesta, Boolean esCorrecta) {
        this.id = id;
        this.idEvaluacion = idEvaluacion;
        this.idEjercicio = idEjercicio;
        this.respuesta = respuesta;
        this.esCorrecta = esCorrecta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEvaluacion() {
        return idEvaluacion;
    }

    public void setIdEvaluacion(Long idEvaluacion) {
        this.idEvaluacion = idEvaluacion;
    }

    public Long getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(Long idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Boolean getEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(Boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }
}
