package pe.edu.upc.educacionparatodos.dtos;

public class TemaDTO {

    private Long id;
    private String materia;
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
