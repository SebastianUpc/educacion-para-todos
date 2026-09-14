package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TemaDTO {

    private Long id;

    @NotBlank(message = "La materia es obligatoria")
    @Size(max = 100, message = "La materia no puede superar los 100 caracteres")
    private String materia;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    public TemaDTO() {
    }

    public TemaDTO(Long id, String materia, String nombre) {
        this.id = id;
        this.materia = materia;
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
