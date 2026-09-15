package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "colegios")
public class Colegio {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idColegio;
    @Column(name="nombreColegio",length = 50,nullable = false)
    private String nombreColegio;
    @Column(name="direccionColegio",length = 50,nullable = false)
    private String direccionColegio;
    @Column(name="distritoColegio",length = 50,nullable = false)
    private String distritoColegio;
    @Column(name="fechaCreacionColegio",nullable = false)
    private LocalDateTime fechaCreacionColegio;
    @Column(name="actualizadoEnColegio",nullable = false)
    private LocalDateTime actualizadoEnColegio;



    public Colegio() {
    }

    public Colegio(Long idColegio, String nombreColegio, String direccionColegio, String distritoColegio, LocalDateTime fechaCreacionColegio, LocalDateTime actualizadoEnColegio) {
        this.idColegio = idColegio;
        this.nombreColegio = nombreColegio;
        this.direccionColegio = direccionColegio;
        this.distritoColegio = distritoColegio;
        this.fechaCreacionColegio = fechaCreacionColegio;
        this.actualizadoEnColegio = actualizadoEnColegio;

    }

    public Long getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(Long idColegio) {
        this.idColegio = idColegio;
    }

    public String getNombreColegio() {
        return nombreColegio;
    }

    public void setNombreColegio(String nombreColegio) {
        this.nombreColegio = nombreColegio;
    }

    public String getDireccionColegio() {
        return direccionColegio;
    }

    public void setDireccionColegio(String direccionColegio) {
        this.direccionColegio = direccionColegio;
    }

    public String getDistritoColegio() {
        return distritoColegio;
    }

    public void setDistritoColegio(String distritoColegio) {
        this.distritoColegio = distritoColegio;
    }

    public LocalDateTime getFechaCreacionColegio() {
        return fechaCreacionColegio;
    }

    public void setFechaCreacionColegio(LocalDateTime fechaCreacionColegio) {
        this.fechaCreacionColegio = fechaCreacionColegio;
    }

    public LocalDateTime getActualizadoEnColegio() {
        return actualizadoEnColegio;
    }

    public void setActualizadoEnColegio(LocalDateTime actualizadoEnColegio) {
        this.actualizadoEnColegio = actualizadoEnColegio;
    }
}

