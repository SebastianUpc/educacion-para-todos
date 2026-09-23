package pe.edu.upc.educacionparatodos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.educacionparatodos.entities.Colegio;

import java.util.List;

@Repository
public interface IColegioRepository extends JpaRepository<Colegio, Long> {

    List<Colegio> findAllByOrderByNombreAsc();

    @Query(value = "select c.distrito, count(c.id)\n" +
            " from colegios c\n" +
            " group by c.distrito", nativeQuery = true)
    List<Object[]> contarColegiosPorDistrito();
}
