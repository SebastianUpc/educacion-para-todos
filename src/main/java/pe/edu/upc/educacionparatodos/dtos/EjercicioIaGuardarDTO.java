package pe.edu.upc.educacionparatodos.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

// Ejercicios que el docente aprobo despues de revisarlos (HU-105, CA07)
public class EjercicioIaGuardarDTO {

    @NotEmpty(message = "Debe seleccionar al menos un ejercicio para guardar")
    @Size(max = 5, message = "Se pueden guardar como maximo 5 ejercicios a la vez")
    private List<@Valid EjercicioInsertDTO> ejercicios;

    public List<EjercicioInsertDTO> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(List<EjercicioInsertDTO> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
