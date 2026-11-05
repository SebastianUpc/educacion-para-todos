package org.example.demo1.Repositories;

import org.example.demo1.Entities.Temas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITemasRepository extends JpaRepository <Temas,Integer> {
}
