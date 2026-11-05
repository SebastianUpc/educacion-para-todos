package org.example.demo1.Entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name ="Temas" )
public class Temas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    public int id_temas;
    @Column(name = "Materia",nullable = false,length = 30)
    public String materia;
    @Column(name = "Nombre",nullable = false,length = 30)
    public String nombreT;
    @Column(name = "crear",nullable = false,length = 30)
    public LocalDate created_at;
    @Column(name = "update",nullable = false,length = 30)
    public LocalDate updated_at;

    public Temas() {

    }

    public Temas(int id_temas, String materia, String nombreT, LocalDate created_at, LocalDate updated_at) {
        this.id_temas = id_temas;
        this.materia = materia;
        this.nombreT = nombreT;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId_temas() {
        return id_temas;
    }

    public void setId_temas(int id_temas) {
        this.id_temas = id_temas;
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
}
