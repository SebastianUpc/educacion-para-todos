package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EjercicioDTO {

    private Long id;

    @NotNull(message = "El tema es obligatorio")
    private Long idTema;

    @NotBlank(message = "El enunciado es obligatorio")
    private String enunciado;

    private String contenidoJson;

    private Integer nivelDificultad;

    private Boolean generadoPorIa;

    @Size(max = 20, message = "El estado no puede superar los 20 caracteres")
    private String estado;

    public EjercicioDTO() {
    }

    public EjercicioDTO(Long id, Long idTema, String enunciado, String contenidoJson, Integer nivelDificultad,
                         Boolean generadoPorIa, String estado) {
        this.id = id;
        this.idTema = idTema;
        this.enunciado = enunciado;
        this.contenidoJson = contenidoJson;
        this.nivelDificultad = nivelDificultad;
        this.generadoPorIa = generadoPorIa;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdTema() {
        return idTema;
    }

    public void setIdTema(Long idTema) {
        this.idTema = idTema;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getContenidoJson() {
        return contenidoJson;
    }

    public void setContenidoJson(String contenidoJson) {
        this.contenidoJson = contenidoJson;
    }

    public Integer getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(Integer nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public Boolean getGeneradoPorIa() {
        return generadoPorIa;
    }

    public void setGeneradoPorIa(Boolean generadoPorIa) {
        this.generadoPorIa = generadoPorIa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
