package pe.edu.upc.educacionparatodos.dtos;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class AulaInsertDTO {

    private Long id;

    @NotNull(message = "El colegio es obligatorio")
    private Long idColegio;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    @NotBlank(message = "La sección es obligatoria")
    private String seccion;
    @NotBlank(message = "El estado es obligatorio")
    private boolean estado;

    private LocalDateTime fechaCreacion;
    private LocalDateTime actualizadoEn;



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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}