package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.educacionparatodos.dtos.EjercicioDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaGuardarDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaRequestDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioIaResponseDTO;
import pe.edu.upc.educacionparatodos.dtos.EstudianteResumenDTO;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAlcanceAcademicoService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioIaService;

import java.util.List;

// HU-105: ejercicios personalizados mediante inteligencia artificial.
// Flujo: el docente elige estudiante y tema -> /generar devuelve propuestas sin
// guardarlas -> el docente revisa y aprueba -> /guardar las inserta en ejercicios.
@RestController
@RequestMapping("/api/ejercicios-ia")
@SecurityRequirement(name = "bearerAuth")
public class EjercicioIaController {

    private final IEjercicioIaService iaS;
    private final IAlcanceAcademicoService alcanceS;

    public EjercicioIaController(IEjercicioIaService iaS, IAlcanceAcademicoService alcanceS) {
        this.iaS = iaS;
        this.alcanceS = alcanceS;
    }

    @Operation(summary = "Estudiantes para los que el usuario puede pedir ejercicios (los de su aula si es docente)")
    @GetMapping("/estudiantes")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<EstudianteResumenDTO>> listarEstudiantes() {
        List<EstudianteResumenDTO> lista = alcanceS.estudiantesVisibles()
                .stream()
                .map(EstudianteResumenDTO::de)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Pide a la IA ejercicios personalizados. No los guarda: son para revisión del docente")
    @PostMapping("/generar")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<EjercicioIaResponseDTO> generar(@Valid @RequestBody EjercicioIaRequestDTO dto) {
        return ResponseEntity.ok(iaS.generar(dto));
    }

    @Operation(summary = "Guarda los ejercicios aprobados por el docente con generadoPorIa = true")
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<EjercicioDTO>> guardar(@Valid @RequestBody EjercicioIaGuardarDTO dto) {
        List<EjercicioDTO> guardados = iaS.guardar(dto.getEjercicios())
                .stream()
                .map(this::convertirADTO)
                .toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(guardados);
    }

    private EjercicioDTO convertirADTO(Ejercicio e) {
        EjercicioDTO dto = new EjercicioDTO();
        dto.setId(e.getId());
        dto.setIdTema(e.getTema() != null ? e.getTema().getId() : null);
        dto.setNombreTema(e.getTema() != null ? e.getTema().getNombre() : null);
        dto.setEnunciado(e.getEnunciado());
        dto.setContenidoJson(e.getContenidoJson());
        dto.setNivelDificultad(e.getNivelDificultad());
        dto.setGeneradoPorIa(e.getGeneradoPorIa());
        dto.setEstado(e.getEstado());
        return dto;
    }
}
