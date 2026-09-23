package org.example.demo1.ServicesInterfaces;

import org.example.demo1.Entities.Temas;

import java.util.List;
import java.util.Optional;

public interface ITemasServices {
    List<Temas> list();
    public Temas insert(Temas temas);
    public void update (Temas temas);
    public void delete (int id);
    public Optional <Temas> listid (int id);
}
