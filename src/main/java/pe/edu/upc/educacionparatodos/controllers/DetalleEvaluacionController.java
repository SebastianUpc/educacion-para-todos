package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.educacionparatodos.dtos.DetalleEvaluacionDTO;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/detalles-evaluacion")
@SecurityRequirement(name = "bearerAuth")
public class DetalleEvaluacionController {

    private final List<DetalleEvaluacionDTO> detalles = new ArrayList<>();

    public DetalleEvaluacionController() {
        detalles.add(new DetalleEvaluacionDTO(1L, 10L, 100L, "Respuesta 1", true));
        detalles.add(new DetalleEvaluacionDTO(2L, 10L, 101L, "Respuesta 2", false));
        detalles.add(new DetalleEvaluacionDTO(3L, 11L, 102L, "Respuesta 3", true));
        detalles.add(new DetalleEvaluacionDTO(4L, 12L, 100L, "Respuesta 4", false));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<DetalleEvaluacionDTO>> listar() {
        return ResponseEntity.ok(detalles);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<DetalleEvaluacionDTO> buscarId(@PathVariable Long id) {
        for (DetalleEvaluacionDTO d : detalles) {
            if (d.getId().equals(id)) {
                return ResponseEntity.ok(d);
            }
        }
        throw new ResourceNotFoundException("No existe un detalle de evaluación con el id: " + id);
    }
}