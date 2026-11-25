package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.dtos.EjercicioIaRequestDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaResponseDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioInsertDTO;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;

import java.util.List;

public interface IEjercicioIaService {

    // Pide los ejercicios a la IA. No guarda nada: el docente los revisa primero.
    EjercicioIaResponseDTO generar(EjercicioIaRequestDTO request);

    // Guarda los ejercicios aprobados con generadoPorIa = true
    List<Ejercicio> guardar(List<EjercicioInsertDTO> ejercicios);
}
