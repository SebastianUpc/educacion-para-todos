package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.DetalleEvaluacionDTO;
import pe.edu.upc.educacionparatodos.entities.DetalleEvaluacion;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.entities.Evaluacion;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IDetalleEvaluacionService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEvaluacionService;

import java.net.URI;
import java.util.List;
import java.util.Locale;

// Antes devolvia una lista fija en memoria. Ahora cada respuesta se guarda en la
// tabla detalles_evaluacion: es la fuente del rendimiento que muestra el dashboard.
@RestController
@RequestMapping("/api/detalles-evaluacion")
@SecurityRequirement(name = "bearerAuth")
public class DetalleEvaluacionController {

    private static final String ESTADO_FINALIZADA = "FINALIZADA";

    private final IDetalleEvaluacionService dS;
    private final IEvaluacionService evS;
    private final IEjercicioService ejS;

    public DetalleEvaluacionController(IDetalleEvaluacionService dS, IEvaluacionService evS,
                                       IEjercicioService ejS) {
        this.dS = dS;
        this.evS = evS;
        this.ejS = ejS;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<DetalleEvaluacionDTO>> listar() {
        List<DetalleEvaluacionDTO> lista = dS.list()
                .stream()
                .map(this::convertirADTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<DetalleEvaluacionDTO> buscarId(@PathVariable Long id) {
        DetalleEvaluacion detalle = dS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un detalle de evaluación con el id: " + id));
        return ResponseEntity.ok(convertirADTO(detalle));
    }

    // Registra la respuesta de un estudiante. esCorrecta no se recibe del cliente:
    // se calcula comparando con la respuesta correcta del ejercicio, para que nadie
    // pueda marcar como correcta una respuesta que no lo es.
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE')")
    public ResponseEntity<DetalleEvaluacionDTO> registrar(@Valid @RequestBody DetalleEvaluacionDTO dto) {
        Evaluacion evaluacion = evS.listId(dto.getIdEvaluacion())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una evaluación con el id: " + dto.getIdEvaluacion()));

        if (ESTADO_FINALIZADA.equals(evaluacion.getEstado())) {
            throw new BusinessRuleException("La evaluación ya está finalizada y no admite más respuestas");
        }

        Ejercicio ejercicio = ejS.findById(dto.getIdEjercicio());

        DetalleEvaluacion detalle = new DetalleEvaluacion();
        detalle.setEvaluacion(evaluacion);
        detalle.setEjercicio(ejercicio);
        detalle.setRespuesta(dto.getRespuesta());
        detalle.setEsCorrecta(normalizar(dto.getRespuesta()).equals(normalizar(ejercicio.getRespuestaCorrecta())));

        dS.insert(detalle);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(detalle.getId())
                .toUri();

        return ResponseEntity.created(location).body(convertirADTO(detalle));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        DetalleEvaluacion detalle = dS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un detalle de evaluación con el id: " + id));
        dS.delete(detalle.getId());
        return ResponseEntity.noContent().build();
    }

    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
    }

    private DetalleEvaluacionDTO convertirADTO(DetalleEvaluacion d) {
        return new DetalleEvaluacionDTO(
                d.getId(),
                d.getEvaluacion() != null ? d.getEvaluacion().getId() : null,
                d.getEjercicio() != null ? d.getEjercicio().getId() : null,
                d.getRespuesta(),
                d.getEsCorrecta());
    }
}
