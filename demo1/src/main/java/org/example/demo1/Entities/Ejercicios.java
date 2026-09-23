package org.example.demo1.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name ="Ejercicios")
public class Ejercicios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int idEjercicio;

    @Column(name = "Enunciado",nullable = false,length = 30)
    public String enunciado;
    @Column(name = "Contenido",nullable = false,length = 30)
    public String contenido;
    @Column(name = "Respuesta",nullable = false,length = 30)
    public String respueta;
    @Column(name = "NivelDificultad",nullable = false)
    public int nivelDificultad;
    @Column(name = "GeneradoIA",nullable = false)
    public boolean generadoIA;
    @Column(name = "Estado",nullable = false, length = 30)
    public String estado;
    @Column(name = "created_at",nullable = false)
    public LocalDate created_at;
    @Column(name = "updated_at",nullable = false)
    public LocalDate updated_at;
    @ManyToOne
    @JoinColumn(name = "idTema",nullable = false)
    public Temas temas;

    public Ejercicios() {
    }
    public Ejercicios(int idEjercicio, String enunciado, String contenido, String respueta, int nivelDificultad, boolean generadoIA, String estado, LocalDate created_at, LocalDate updated_at, Temas temas) {
        this.idEjercicio = idEjercicio;
        this.enunciado = enunciado;
        this.contenido = contenido;
        this.respueta = respueta;
        this.nivelDificultad = nivelDificultad;
        this.generadoIA = generadoIA;
        this.estado = estado;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.temas = temas;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getRespueta() {
        return respueta;
    }

    public void setRespueta(String respueta) {
        this.respueta = respueta;
    }

    public int getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(int nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public boolean isGeneradoIA() {
        return generadoIA;
    }

    public void setGeneradoIA(boolean generadoIA) {
        this.generadoIA = generadoIA;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public Temas getTemas() {
        return temas;
    }

    public void setTemas(Temas temas) {
        this.temas = temas;
    }
}
