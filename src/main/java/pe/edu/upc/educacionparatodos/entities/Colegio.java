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
    private Long id;
    @Column(name="nombreColegio",length = 50,nullable = false)
    private String nombre;
    @Column(name="direccionColegio",length = 50,nullable = false)
    private String direccion;
    @Column(name="distritoColegio",length = 50,nullable = false)
    private String distrito;
    @Column(name="fechaCreacionColegio",nullable = false)
    private LocalDateTime fechaCreacionColegio;
    @Column(name="actualizadoEnColegio",nullable = false)
    private LocalDateTime actualizadoEnColegio;



    public Colegio() {
    }

    public Colegio(Long id, String nombre, String direccion, String distrito, LocalDateTime fechaCreacionColegio, LocalDateTime actualizadoEnColegio) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.distrito = distrito;
        this.fechaCreacionColegio = fechaCreacionColegio;
        this.actualizadoEnColegio = actualizadoEnColegio;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
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

