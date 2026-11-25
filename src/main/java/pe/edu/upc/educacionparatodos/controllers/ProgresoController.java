package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.ProgresoDTO;
import pe.edu.upc.educacionparatodos.entities.Progreso;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IProgresoService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

// Antes devolvia una lista fija en memoria. Ahora el progreso se guarda en la
// tabla progresos porque lo consumen la IA (HU-105) y el dashboard (HU-106).
@RestController
@RequestMapping("/api/progresos")
@SecurityRequirement(name = "bearerAuth")
public class ProgresoController {

    private final IProgresoService pS;
    private final IUsuarioService uS;
    private final ITemaService tS;

    public ProgresoController(IProgresoService pS, IUsuarioService uS, ITemaService tS) {
        this.pS = pS;
        this.uS = uS;
        this.tS = tS;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<ProgresoDTO>> listar() {
        List<ProgresoDTO> lista = pS.list()
                .stream()
                .map(this::convertirADTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<ProgresoDTO> buscarId(@PathVariable Long id) {
        Progreso progreso = pS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un progreso con el id: " + id));
        return ResponseEntity.ok(convertirADTO(progreso));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<ProgresoDTO> registrar(@Valid @RequestBody ProgresoDTO dto) {
        Usuario estudiante = uS.listId(dto.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estudiante con el id: " + dto.getIdEstudiante()));
        Tema tema = tS.findById(dto.getIdTema());

        Progreso progreso = new Progreso();
        progreso.setEstudiante(estudiante);
        progreso.setTema(tema);
        progreso.setNivelActual(dto.getNivelActual());
        progreso.setPorcentajeAvance(dto.getPorcentajeAvance());
        progreso.setDiagnosticoIa(dto.getDiagnosticoIa());

        pS.insert(progreso);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(progreso.getId())
                .toUri();

        return ResponseEntity.created(location).body(convertirADTO(progreso));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Progreso progreso = pS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un progreso con el id: " + id));
        pS.delete(progreso.getId());
        return ResponseEntity.noContent().build();
    }

    private ProgresoDTO convertirADTO(Progreso p) {
        ProgresoDTO dto = new ProgresoDTO(
                p.getId(),
                p.getEstudiante() != null ? p.getEstudiante().getId() : null,
                p.getTema() != null ? p.getTema().getId() : null,
                p.getNivelActual(),
                p.getPorcentajeAvance(),
                p.getDiagnosticoIa());
        if (p.getEstudiante() != null) {
            dto.setNombreEstudiante(p.getEstudiante().getNombre() + " " + p.getEstudiante().getApellido());
        }
        if (p.getTema() != null) {
            dto.setNombreTema(p.getTema().getNombre());
        }
        return dto;
    }
}
