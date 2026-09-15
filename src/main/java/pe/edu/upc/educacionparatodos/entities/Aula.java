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
    private Long id;
    @Column(name="gradoAula",length = 10,nullable = false)
    private String grado;
    @Column(name="seccionAula",length = 10, nullable = false)
    private String seccion;
    @Column(name="estadoAula",nullable = false)
    private boolean estado;
    @Column(name="fechaCreacionAula",nullable = false)
    private LocalDateTime fechaCreacionAula;
    @Column(name="actualizadoEnAula",nullable = false)
    private LocalDateTime actualizadoEnAula;
    @ManyToOne
    @JoinColumn(name="idColegio")
    private Colegio colegio;

    public Aula() {
    }

    public Aula(Long id, String grado, String seccion, boolean estado, LocalDateTime fechaCreacionAula, LocalDateTime actualizadoEnAula, Colegio colegio) {
        this.id = id;
        this.grado = grado;
        this.seccion = seccion;
        this.estado = estado;
        this.fechaCreacionAula = fechaCreacionAula;
        this.actualizadoEnAula = actualizadoEnAula;
        this.colegio = colegio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
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
}
