package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRol;
    @Column(name="nombreRol",length = 50,nullable = false)
    private String nombreRol;
    @Column(name="descripcionRol",length = 100,nullable = false)
    private String descripcionRol;
    @Column(name="fechaCreacionRol",nullable = false)
    private LocalDateTime fechaCreacionRol;
    @Column(name="actualizadoEnRol",nullable = false)
    private LocalDateTime actualizadoEnRol;

    public Rol() {
    }

    public Rol(Long idRol, String nombreRol, String descripcionRol, LocalDateTime fechaCreacionRol, LocalDateTime actualizadoEnRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
        this.descripcionRol = descripcionRol;
        this.fechaCreacionRol = fechaCreacionRol;
        this.actualizadoEnRol = actualizadoEnRol;
    }

    public Long getIdRol() {
        return idRol;
    }

    public void setIdRol(Long idRol) {
        this.idRol = idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }

    public String getDescripcionRol() {
        return descripcionRol;
    }

    public void setDescripcionRol(String descripcionRol) {
        this.descripcionRol = descripcionRol;
    }

    public LocalDateTime getFechaCreacionRol() {
        return fechaCreacionRol;
    }

    public void setFechaCreacionRol(LocalDateTime fechaCreacionRol) {
        this.fechaCreacionRol = fechaCreacionRol;
    }

    public LocalDateTime getActualizadoEnRol() {
        return actualizadoEnRol;
    }

    public void setActualizadoEnRol(LocalDateTime actualizadoEnRol) {
        this.actualizadoEnRol = actualizadoEnRol;
    }
}
