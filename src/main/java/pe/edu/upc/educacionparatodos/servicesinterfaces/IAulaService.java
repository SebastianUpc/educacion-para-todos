package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.entities.Aula;

import java.util.List;
import java.util.Optional;

public interface IAulaService {

    void insert(Aula u);

    List<Aula> list();

    Optional<Aula> listId(Long id);

    void update(Aula u);

    void delete(Long id);

    List<Object[]> contarAulasPorColegio();
}
