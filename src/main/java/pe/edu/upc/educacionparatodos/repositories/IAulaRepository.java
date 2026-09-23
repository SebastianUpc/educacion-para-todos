package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Aula;

import java.util.List;

@Repository
public interface IAulaRepository extends JpaRepository<Aula, Long> {

    boolean existsByColegioId(Long colegioId);

    @Query(value = "select c.nombre, count(a.id)\n" +
            " from colegios c left join aulas a\n" +
            " on c.id = a.id_colegio\n" +
            " group by c.nombre", nativeQuery = true)
    List<Object[]> contarAulasPorColegio();
}
