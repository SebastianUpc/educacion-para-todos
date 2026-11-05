package org.example.demo1.ServicesImplements;

import org.example.demo1.Entities.Ejercicios;
import org.example.demo1.Repositories.IEjercicioRepository;
import org.example.demo1.ServicesInterfaces.IEjercicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EjerciciosServiceImplement implements IEjercicioService {

    @Autowired
    private IEjercicioRepository eR;

    @Override
    public List<Ejercicios> list() {
        return eR.findAll();
    }

    @Override
    public Ejercicios insert(Ejercicios ejercicios) {
        return eR.save(ejercicios);
    }

    @Override
    public void update(Ejercicios ejercicios) {
        eR.save(ejercicios);
    }

    @Override
    public void delete(int id) {
        eR.deleteById(id);
    }

    @Override
    public Optional<Ejercicios> listid(int id) {
        return eR.findById(id);
    }
}
