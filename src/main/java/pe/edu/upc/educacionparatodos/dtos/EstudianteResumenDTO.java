package pe.edu.upc.educacionparatodos.dtos;

import pe.edu.upc.educacionparatodos.entities.Usuario;

// Datos minimos de un estudiante para desplegables y tablas (HU-105 y HU-106).
// No expone email, password ni apoderado.
public class EstudianteResumenDTO {

    private Long id;
    private String nombreCompleto;
    private String aula;

    public EstudianteResumenDTO() {
    }

    public static EstudianteResumenDTO de(Usuario u) {
        EstudianteResumenDTO dto = new EstudianteResumenDTO();
        dto.setId(u.getId());
        dto.setNombreCompleto(u.getNombre() + " " + u.getApellido());
        dto.setAula(u.getAula() != null ? u.getAula().getGrado() + " " + u.getAula().getSeccion() : null);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }
}
