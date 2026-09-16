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

@Entity
@Table(name = "aulas")
public class Aula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="grado", length = 10, nullable = false)
    private String grado;
    @Column(name="seccion", length = 10, nullable = false)
    private String seccion;
    @Column(name="estado", nullable = false)
    private boolean estado;
    @Column(name="fecha_creacion_aula", nullable = false)
    private LocalDateTime fechaCreacionAula;
    @Column(name="actualizado_en_aula", nullable = false)
    private LocalDateTime actualizadoEnAula;
    @ManyToOne
    @JoinColumn(name = "id_colegio")
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

    @PrePersist
    protected void onCreate() {
        this.fechaCreacionAula = LocalDateTime.now();
        this.actualizadoEnAula = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEnAula = LocalDateTime.now();
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
