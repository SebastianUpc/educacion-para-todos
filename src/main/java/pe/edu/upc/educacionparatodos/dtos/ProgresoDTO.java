package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProgresoDTO {

    private Long id;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "El tema es obligatorio")
    private Long idTema;

    private Integer nivelActual;

    private BigDecimal porcentajeAvance;

    private String diagnosticoIa;

    public ProgresoDTO() {
    }

    public ProgresoDTO(Long id, Long idEstudiante, Long idTema, Integer nivelActual, BigDecimal porcentajeAvance,
                        String diagnosticoIa) {
        this.id = id;
        this.idEstudiante = idEstudiante;
        this.idTema = idTema;
        this.nivelActual = nivelActual;
        this.porcentajeAvance = porcentajeAvance;
        this.diagnosticoIa = diagnosticoIa;
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

    public Long getIdTema() {
        return idTema;
    }

    public void setIdTema(Long idTema) {
        this.idTema = idTema;
    }

    public Integer getNivelActual() {
        return nivelActual;
    }

    public void setNivelActual(Integer nivelActual) {
        this.nivelActual = nivelActual;
    }

    public BigDecimal getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(BigDecimal porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getDiagnosticoIa() {
        return diagnosticoIa;
    }

    public void setDiagnosticoIa(String diagnosticoIa) {
        this.diagnosticoIa = diagnosticoIa;
    }
}
