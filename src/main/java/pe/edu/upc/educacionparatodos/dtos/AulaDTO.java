package pe.edu.upc.educacionparatodos.dtos;

import java.time.LocalDateTime;

public class AulaDTO {

    private Long idAula;
    private Long idColegio;
    private String gradoAula;
    private String seccionAula;
    private boolean estadoAula;
    private LocalDateTime fechaCreacion;
    private LocalDateTime actualizadoEn;

    public Long getIdAula() {
        return idAula;
    }

    public void setIdAula(Long idAula) {
        this.idAula = idAula;
    }

    public Long getIdColegio() {
        return idColegio;
    }

    public void setIdColegio(Long idColegio) {
        this.idColegio = idColegio;
    }

    public String getGradoAula() {
        return gradoAula;
    }

    public void setGradoAula(String gradoAula) {
        this.gradoAula = gradoAula;
    }

    public String getSeccionAula() {
        return seccionAula;
    }

    public void setSeccionAula(String seccionAula) {
        this.seccionAula = seccionAula;
    }

    public boolean isEstadoAula() {
        return estadoAula;
    }

    public void setEstadoAula(boolean estadoAula) {
        this.estadoAula = estadoAula;
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

}

