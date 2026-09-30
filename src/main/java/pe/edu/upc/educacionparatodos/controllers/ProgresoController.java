package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.educacionparatodos.dtos.ProgresoDTO;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/progresos")
@SecurityRequirement(name = "bearerAuth")
public class ProgresoController {

    private final List<ProgresoDTO> progresos = new ArrayList<>();

    public ProgresoController() {
        progresos.add(new ProgresoDTO(1L, 101L, 201L, 1, new BigDecimal("25.50"), "Necesita reforzar sumas"));
        progresos.add(new ProgresoDTO(2L, 102L, 202L, 2, new BigDecimal("60.00"), "Buen avance en lectura"));
        progresos.add(new ProgresoDTO(3L, 103L, 201L, 3, new BigDecimal("80.25"), "Domina el tema"));
        progresos.add(new ProgresoDTO(4L, 101L, 203L, 2, new BigDecimal("45.75"), "Avance regular"));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<ProgresoDTO>> listar() {
        return ResponseEntity.ok(progresos);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<ProgresoDTO> buscarId(@PathVariable Long id) {
        for (ProgresoDTO p : progresos) {
            if (p.getId().equals(id)) {
                return ResponseEntity.ok(p);
            }
        }
        throw new ResourceNotFoundException("No existe un progreso con el id: " + id);
    }
}