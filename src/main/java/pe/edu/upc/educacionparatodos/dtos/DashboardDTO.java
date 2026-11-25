package pe.edu.upc.educacionparatodos.dtos;

import java.util.List;

// HU-106: indicadores del dashboard de seguimiento academico (T04)
public class DashboardDTO {

    // Que estudiantes se incluyen: el aula del docente o todas (ADMIN)
    private String alcance;

    // CA08: false si no hay respuestas ni progreso registrados
    private boolean hayDatos;

    private long totalEstudiantes;

    private long totalEvaluaciones;

    private long evaluacionesFinalizadas;

    private long evaluacionesEnCurso;

    private long totalRespuestas;

    // CA04: nota vigesimal (0 a 20) del grupo
    private Double promedioAcademico;

    private Double porcentajeAciertos;

    private Double progresoPromedio;

    private Double notaMinimaAprobatoria;

    private List<RendimientoTemaDTO> rendimientoPorTema;

    private List<EstudianteRendimientoDTO> estudiantesConDificultades;

    public DashboardDTO() {
    }

    public String getAlcance() {
        return alcance;
    }

    public void setAlcance(String alcance) {
        this.alcance = alcance;
    }

    public boolean isHayDatos() {
        return hayDatos;
    }

    public void setHayDatos(boolean hayDatos) {
        this.hayDatos = hayDatos;
    }

    public long getTotalEstudiantes() {
        return totalEstudiantes;
    }

    public void setTotalEstudiantes(long totalEstudiantes) {
        this.totalEstudiantes = totalEstudiantes;
    }

    public long getTotalEvaluaciones() {
        return totalEvaluaciones;
    }

    public void setTotalEvaluaciones(long totalEvaluaciones) {
        this.totalEvaluaciones = totalEvaluaciones;
    }

    public long getEvaluacionesFinalizadas() {
        return evaluacionesFinalizadas;
    }

    public void setEvaluacionesFinalizadas(long evaluacionesFinalizadas) {
        this.evaluacionesFinalizadas = evaluacionesFinalizadas;
    }

    public long getEvaluacionesEnCurso() {
        return evaluacionesEnCurso;
    }

    public void setEvaluacionesEnCurso(long evaluacionesEnCurso) {
        this.evaluacionesEnCurso = evaluacionesEnCurso;
    }

    public long getTotalRespuestas() {
        return totalRespuestas;
    }

    public void setTotalRespuestas(long totalRespuestas) {
        this.totalRespuestas = totalRespuestas;
    }

    public Double getPromedioAcademico() {
        return promedioAcademico;
    }

    public void setPromedioAcademico(Double promedioAcademico) {
        this.promedioAcademico = promedioAcademico;
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

    public Double getNotaMinimaAprobatoria() {
        return notaMinimaAprobatoria;
    }

    public void setNotaMinimaAprobatoria(Double notaMinimaAprobatoria) {
        this.notaMinimaAprobatoria = notaMinimaAprobatoria;
    }

    public List<RendimientoTemaDTO> getRendimientoPorTema() {
        return rendimientoPorTema;
    }

    public void setRendimientoPorTema(List<RendimientoTemaDTO> rendimientoPorTema) {
        this.rendimientoPorTema = rendimientoPorTema;
    }

    public List<EstudianteRendimientoDTO> getEstudiantesConDificultades() {
        return estudiantesConDificultades;
    }

    public void setEstudiantesConDificultades(List<EstudianteRendimientoDTO> estudiantesConDificultades) {
        this.estudiantesConDificultades = estudiantesConDificultades;
    }
}
