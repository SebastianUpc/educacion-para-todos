package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Aula;

import java.util.List;

@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long> {

    boolean existsByColegioId(Long colegioId);

    // Se agrupa por c.id y no por el nombre: el nombre del colegio no es unico,
    // asi que dos colegios homonimos en distintos distritos se fusionarian.
    @Query(value = "select c.nombre, count(a.id)\n" +
            " from colegios c left join aulas a\n" +
            " on c.id = a.id_colegio\n" +
            " group by c.id, c.nombre\n" +
            " order by count(a.id) desc", nativeQuery = true)
    List<Object[]> contarAulasPorColegio();
}
