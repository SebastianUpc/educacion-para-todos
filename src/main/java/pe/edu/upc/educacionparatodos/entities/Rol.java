package pe.edu.upc.educacionparatodos.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre_rol",length = 50,nullable = false)
    private String nombre;
    @Column(name="descripcion_rol",length = 100,nullable = false)
    private String descripcionRol;
    @Column(name="fecha_creacion_rol", nullable = false)
    private LocalDateTime fechaCreacionRol;
    @Column(name="actualizado_en_rol", nullable = false)
    private LocalDateTime actualizadoEnRol;

    public Rol() {
    }

    public Rol(Long id, String nombre, String descripcionRol, LocalDateTime fechaCreacionRol, LocalDateTime actualizadoEnRol) {
        this.id = id;
        this.nombre = nombre;
        this.descripcionRol = descripcionRol;
        this.fechaCreacionRol = fechaCreacionRol;
        this.actualizadoEnRol = actualizadoEnRol;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacionRol = LocalDateTime.now();
        this.actualizadoEnRol = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.actualizadoEnRol = LocalDateTime.now();
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

