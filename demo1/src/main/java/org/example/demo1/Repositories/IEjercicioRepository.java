package org.example.demo1.Repositories;

import org.example.demo1.Entities.Ejercicios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEjercicioRepository extends JpaRepository<Ejercicios,Integer> {
}
