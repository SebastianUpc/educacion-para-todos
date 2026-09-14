package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AulaDTO {

    private Long id;

    @NotNull(message = "El colegio es obligatorio")
    private Long idColegio;

    @NotBlank(message = "El grado es obligatorio")
    @Size(max = 20, message = "El grado no puede superar los 20 caracteres")
    private String grado;

    @NotBlank(message = "La sección es obligatoria")
    @Size(max = 5, message = "La sección no puede superar los 5 caracteres")
    private String seccion;

    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;

    public AulaDTO() {
    }

    public AulaDTO(Long id, Long idColegio, String grado, String seccion, String estado) {
        this.id = id;
        this.idColegio = idColegio;
        this.grado = grado;
        this.seccion = seccion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(Long idColegio) {
        this.idColegio = idColegio;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
