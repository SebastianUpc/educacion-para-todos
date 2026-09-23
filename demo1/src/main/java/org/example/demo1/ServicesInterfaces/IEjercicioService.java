package org.example.demo1.ServicesInterfaces;

import org.example.demo1.Entities.Ejercicios;
import org.example.demo1.Entities.Temas;

import java.util.List;
import java.util.Optional;

public interface IEjercicioService {
    List<Ejercicios> list();
    public Ejercicios insert(Ejercicios ejercicios);
    public void update (Ejercicios ejercicios);
    public void delete (int id);
    public Optional<Ejercicios> listid (int id);
}
