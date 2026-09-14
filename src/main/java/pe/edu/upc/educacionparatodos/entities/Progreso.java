package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "progreso")
public class Progreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Usuario estudiante;

    @ManyToOne
    @JoinColumn(name = "tema_id", nullable = false)
    private Tema tema;

    @Column(name = "nivel_actual")
    private Integer nivelActual;

    @Column(name = "porcentaje_avance", precision = 5, scale = 2)
    private BigDecimal porcentajeAvance;

    @Lob
    @Column(name = "diagnostico_ia")
    private String diagnosticoIa;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Progreso() {
    }

    public Progreso(Usuario estudiante, Tema tema, Integer nivelActual, BigDecimal porcentajeAvance,
                     String diagnosticoIa) {
        this.estudiante = estudiante;
        this.tema = tema;
        this.nivelActual = nivelActual;
        this.porcentajeAvance = porcentajeAvance;
        this.diagnosticoIa = diagnosticoIa;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Usuario estudiante) {
        this.estudiante = estudiante;
    }

    public Tema getTema() {
        return tema;
    }

    public void setTema(Tema tema) {
        this.tema = tema;
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

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
