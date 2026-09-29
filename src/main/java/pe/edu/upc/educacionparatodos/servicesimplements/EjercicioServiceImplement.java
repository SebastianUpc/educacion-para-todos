package pe.edu.upc.educacionparatodos.servicesimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.repositories.IEjercicioRepository;
import pe.edu.upc.educacionparatodos.repositories.ITemaRepository;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;

import java.util.List;

@Service
public class EjercicioServiceImplement implements IEjercicioService {

    private final IEjercicioRepository ejercicioRepository;
    private final ITemaRepository temaRepository;

    public EjercicioServiceImplement(IEjercicioRepository ejercicioRepository, ITemaRepository temaRepository) {
        this.ejercicioRepository = ejercicioRepository;
        this.temaRepository = temaRepository;
    }

    @Override
    public List<Ejercicio> list() {
        return ejercicioRepository.findAll();
    }

    @Override
    public Ejercicio findById(Long id) {
        return ejercicioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un ejercicio con el id: " + id
                ));
    }

    @Override
    public Ejercicio insert(Ejercicio e) {
        e.setTema(buscarTema(e.getTema()));
        return ejercicioRepository.save(e);
    }

    @Override
    public Ejercicio update(Long id, Ejercicio e) {
        Ejercicio existente = findById(id);

        existente.setTema(buscarTema(e.getTema()));
        existente.setEnunciado(e.getEnunciado());
        existente.setContenidoJson(e.getContenidoJson());
        existente.setRespuestaCorrecta(e.getRespuestaCorrecta());
        existente.setNivelDificultad(e.getNivelDificultad());
        existente.setGeneradoPorIa(e.getGeneradoPorIa());
        existente.setEstado(e.getEstado());

        return ejercicioRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        Ejercicio ejercicio = findById(id);
        ejercicioRepository.deleteById(ejercicio.getId());
    }

    private Tema buscarTema(Tema tema) {
        Long idTema = tema != null ? tema.getId() : null;
        if (idTema == null) {
            throw new ResourceNotFoundException("El ejercicio debe tener un tema asociado");
        }
        return temaRepository.findById(idTema)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tema con el id: " + idTema
                ));
    }
}
