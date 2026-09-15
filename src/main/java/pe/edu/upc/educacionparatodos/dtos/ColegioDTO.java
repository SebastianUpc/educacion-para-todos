package pe.edu.upc.educacionparatodos.dtos;

import java.time.LocalDateTime;

public class ColegioDTO {

    private Long idColegio;
    private String nombreColegio;
    private String direccionColegio;
    private String distritoColegio;
    private LocalDateTime fechaCreacion;
    private LocalDateTime actualizadoEn;

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getActualizadoEn() {
        return actualizadoEn;
    }

    public void setActualizadoEn(LocalDateTime actualizadoEn) {
        this.actualizadoEn = actualizadoEn;
    }

    public Long getId() { return this.idColegio; }
    public void setId(Long id) { this.idColegio = id; }
    public String getNombre() { return this.nombreColegio; }
    public void setNombre(String nombre) { this.nombreColegio = nombre; }
}

