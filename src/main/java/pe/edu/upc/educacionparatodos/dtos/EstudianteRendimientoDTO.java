package pe.edu.upc.educacionparatodos.dtos;

import java.util.List;

// HU-106 CA06: estudiante que necesita refuerzo y por que
public class EstudianteRendimientoDTO {

    private Long idEstudiante;

    private String nombreCompleto;

    private String aula;

    private long totalRespuestas;

    // Nota vigesimal (0 a 20); null si aun no tiene respuestas
    private Double promedio;

    private Double porcentajeAciertos;

    // Avance promedio registrado en la tabla progresos
    private Double progresoPromedio;

    private String temaMasDebil;

    private List<String> motivos;

    public EstudianteRendimientoDTO() {
    }

    public Long getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(Long idEstudiante) {
        this.idEstudiante = idEstudiante;
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

    public long getTotalRespuestas() {
        return totalRespuestas;
    }

    public void setTotalRespuestas(long totalRespuestas) {
        this.totalRespuestas = totalRespuestas;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Double getPorcentajeAciertos() {
        return porcentajeAciertos;
    }

    public void setPorcentajeAciertos(Double porcentajeAciertos) {
        this.porcentajeAciertos = porcentajeAciertos;
    }

    public Double getProgresoPromedio() {
        return progresoPromedio;
    }

    public void setProgresoPromedio(Double progresoPromedio) {
        this.progresoPromedio = progresoPromedio;
    }

    public String getTemaMasDebil() {
        return temaMasDebil;
    }

    public void setTemaMasDebil(String temaMasDebil) {
        this.temaMasDebil = temaMasDebil;
    }

    public List<String> getMotivos() {
        return motivos;
    }

    public void setMotivos(List<String> motivos) {
        this.motivos = motivos;
    }
}
