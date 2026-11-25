package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProgresoDTO {

    private Long id;

    @NotNull(message = "El estudiante es obligatorio")
    private Long idEstudiante;

    @NotNull(message = "El tema es obligatorio")
    private Long idTema;

    @Min(value = 1, message = "El nivel debe estar entre 1 y 5")
    @Max(value = 5, message = "El nivel debe estar entre 1 y 5")
    private Integer nivelActual;

    @DecimalMin(value = "0.00", message = "El avance no puede ser negativo")
    @DecimalMax(value = "100.00", message = "El avance no puede superar el 100%")
    private BigDecimal porcentajeAvance;

    private String diagnosticoIa;

    // Solo de lectura: se llenan al responder para no mostrar solo ids en la tabla
    private String nombreEstudiante;

    private String nombreTema;

    public ProgresoDTO() {
    }

    public String getNombreEstudiante() {
        return nombreEstudiante;
    }

    public void setNombreEstudiante(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public String getNombreTema() {
        return nombreTema;
    }

    public void setNombreTema(String nombreTema) {
        this.nombreTema = nombreTema;
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
