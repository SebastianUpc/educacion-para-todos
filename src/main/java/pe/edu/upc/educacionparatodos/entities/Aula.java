package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import org.hibernate.annotations.IdGeneratorType;

import java.time.LocalDateTime;

@Table
@Entity(name = "aulas")
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAula;
    @Column(name="gradoAula",length = 10,nullable = false)
    private String gradoAula;
    @Column(name="seccionAula",length = 10, nullable = false)
    private String seccionAula;
    @Column(name="estadoAula",nullable = false)
    private boolean estadoAula;
    @Column(name="fechaCreacionAula",nullable = false)
    private LocalDateTime fechaCreacionAula;
    @Column(name="actualizadoEnAula",nullable = false)
    private LocalDateTime actualizadoEnAula;
    @ManyToOne
    @JoinColumn(name="idColegio")
    private Colegio colegio;

    public Aula() {
    }

    public Aula(Long idAula, String gradoAula, String seccionAula, boolean estadoAula, LocalDateTime fechaCreacionAula, LocalDateTime actualizadoEnAula, Colegio colegio) {
        this.idAula = idAula;
        this.gradoAula = gradoAula;
        this.seccionAula = seccionAula;
        this.estadoAula = estadoAula;
        this.fechaCreacionAula = fechaCreacionAula;
        this.actualizadoEnAula = actualizadoEnAula;
        this.colegio = colegio;
    }

    public Long getIdAula() {
        return idAula;
    }

    public void setIdAula(Long idAula) {
        this.idAula = idAula;
    }

    public String getGradoAula() {
        return gradoAula;
    }

    public void setGradoAula(String gradoAula) {
        this.gradoAula = gradoAula;
    }

    public String getSeccionAula() {
        return seccionAula;
    }

    public void setSeccionAula(String seccionAula) {
        this.seccionAula = seccionAula;
    }

    public boolean isEstadoAula() {
        return estadoAula;
    }

    public void setEstadoAula(boolean estadoAula) {
        this.estadoAula = estadoAula;
    }

    public LocalDateTime getFechaCreacionAula() {
        return fechaCreacionAula;
    }

    public void setFechaCreacionAula(LocalDateTime fechaCreacionAula) {
        this.fechaCreacionAula = fechaCreacionAula;
    }

    public LocalDateTime getActualizadoEnAula() {
        return actualizadoEnAula;
    }

    public void setActualizadoEnAula(LocalDateTime actualizadoEnAula) {
        this.actualizadoEnAula = actualizadoEnAula;
    }

    public Colegio getColegio() {
        return colegio;
    }

    public void setColegio(Colegio colegio) {
        this.colegio = colegio;
    }

    public Long getId() { return this.idAula; }
    public void setId(Long id) { this.idAula = id; }
    public String getNombre() { return this.gradoAula; } // nombre genérico para compatibilidad
}
