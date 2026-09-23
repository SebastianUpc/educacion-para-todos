package org.example.demo1.DTO;

import jakarta.persistence.Column;

import java.time.LocalDate;

public class TemasDto {
    public int id_temas;
    public String materia;
    public String nombreT;
    public LocalDate created_at;
    public LocalDate updated_at;

    public int getId_temas() {
        return id_temas;
    }

    public void setId_temas(int id_temas) {
        this.id_temas = id_temas;
    }

    public LocalDate getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDate updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDate getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDate created_at) {
        this.created_at = created_at;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public String getNombreT() {
        return nombreT;
    }

    public void setNombreT(String nombreT) {
        this.nombreT = nombreT;
    }
}
