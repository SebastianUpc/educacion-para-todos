package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class RolInsertDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    private LocalDateTime fechaCreacion;
    private LocalDateTime actualizadoEn;

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}