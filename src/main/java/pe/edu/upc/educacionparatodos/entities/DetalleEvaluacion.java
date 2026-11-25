package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

// Respuesta que da un estudiante a un ejercicio dentro de una evaluacion.
// Es la base del rendimiento academico: el dashboard (HU-106) calcula el
// promedio y las dificultades por tema a partir de estos registros.
@Entity
@Table(name = "detalles_evaluacion")
public class DetalleEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "ejercicio_id", nullable = false)
    private Ejercicio ejercicio;

    @Column(columnDefinition = "TEXT")
    private String respuesta;

    @Column(name = "es_correcta", nullable = false)
    private Boolean esCorrecta;

    @Column(name = "fecha_respuesta")
    private LocalDateTime fechaRespuesta;

    public DetalleEvaluacion() {
    }

    @PrePersist
    protected void onCreate() {
        this.fechaRespuesta = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evaluacion getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Evaluacion evaluacion) {
        this.evaluacion = evaluacion;
    }

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
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

    public LocalDateTime getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDateTime fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }
}
