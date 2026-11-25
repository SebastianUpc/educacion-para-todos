package pe.edu.upc.educacionparatodos.dtos;

// HU-106 CA05: rendimiento de los estudiantes en un tema
public class RendimientoTemaDTO {

    private Long idTema;

    private String tema;

    private long totalRespuestas;

    private long respuestasCorrectas;

    private Double porcentajeAciertos;

    // Nota vigesimal (0 a 20) equivalente al porcentaje de aciertos
    private Double promedio;

    public RendimientoTemaDTO() {
    }

    public Long getIdTema() {
        return idTema;
    }

    public void setIdTema(Long idTema) {
        this.idTema = idTema;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public long getTotalRespuestas() {
        return totalRespuestas;
    }

    public void setTotalRespuestas(long totalRespuestas) {
        this.totalRespuestas = totalRespuestas;
    }

    public long getRespuestasCorrectas() {
        return respuestasCorrectas;
    }

    public void setRespuestasCorrectas(long respuestasCorrectas) {
        this.respuestasCorrectas = respuestasCorrectas;
    }

    public Double getPorcentajeAciertos() {
        return porcentajeAciertos;
    }

    public void setPorcentajeAciertos(Double porcentajeAciertos) {
        this.porcentajeAciertos = porcentajeAciertos;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}
