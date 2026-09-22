package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.EvaluacionDTO;
import pe.edu.upc.educacionparatodos.dtos.EvaluacionesPorEstudianteDTO;
import pe.edu.upc.educacionparatodos.entities.Evaluacion;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEvaluacionService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/evaluaciones")
@SecurityRequirement(name = "bearerAuth")
public class EvaluacionController {

    private static final String ESTADO_EN_CURSO = "EN_CURSO";
    private static final String ESTADO_FINALIZADA = "FINALIZADA";

    private final IEvaluacionService eS;
    private final IUsuarioService uS;
    private final ModelMapper modelMapper;

    public EvaluacionController(IEvaluacionService eS, IUsuarioService uS, ModelMapper modelMapper) {
        this.eS = eS;
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<EvaluacionDTO>> listar() {
        List<EvaluacionDTO> lista = eS.list()
                .stream()
                .map(this::convertirADTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> buscarId(@PathVariable Long id) {
        Evaluacion evaluacion = eS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una evaluación con el id: " + id
                ));

        return ResponseEntity.ok(convertirADTO(evaluacion));
    }

    @PostMapping
    public ResponseEntity<EvaluacionDTO> registrar(@Valid @RequestBody EvaluacionDTO dto) {
        Usuario estudiante = uS.listId(dto.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estudiante con el id: " + dto.getIdEstudiante()
                ));

        Evaluacion evaluacion = modelMapper.map(dto, Evaluacion.class);
        evaluacion.setId(null);
        evaluacion.setEstudiante(estudiante);
        evaluacion.setEstado(ESTADO_EN_CURSO);

        eS.insert(evaluacion);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(evaluacion.getId())
                .toUri();

        return ResponseEntity.created(location).body(convertirADTO(evaluacion));
    }

    @PutMapping
    public ResponseEntity<EvaluacionDTO> actualizar(@Valid @RequestBody EvaluacionDTO dto) {
        Evaluacion evaluacion = eS.listId(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una evaluación con el id: " + dto.getId()
                ));

        if (ESTADO_FINALIZADA.equals(evaluacion.getEstado())) {
            throw new BusinessRuleException(
                    "La evaluación ya se encuentra finalizada y no puede modificarse"
            );
        }

        Usuario estudiante = uS.listId(dto.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un estudiante con el id: " + dto.getIdEstudiante()
                ));

        evaluacion.setEstudiante(estudiante);
        evaluacion.setEstado(dto.getEstado());

        if (ESTADO_FINALIZADA.equals(dto.getEstado())) {
            evaluacion.setFechaFin(LocalDateTime.now());
        }

        eS.update(evaluacion);

        return ResponseEntity.ok(convertirADTO(evaluacion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Evaluacion evaluacion = eS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una evaluación con el id: " + id
                ));

        eS.delete(evaluacion.getId());

        return ResponseEntity.noContent().build();
    }

    // Query con JOIN: cuantas evaluaciones tiene rendidas cada estudiante
    @GetMapping("/cantidades")
    public ResponseEntity<List<EvaluacionesPorEstudianteDTO>> contarPorEstudiante() {
        List<EvaluacionesPorEstudianteDTO> lista = eS.contarEvaluacionesPorEstudiante()
                .stream()
                .map(item -> {
                    EvaluacionesPorEstudianteDTO dto = new EvaluacionesPorEstudianteDTO();
                    dto.setNombre((String) item[0]);
                    dto.setCantidad(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    private EvaluacionDTO convertirADTO(Evaluacion evaluacion) {
        EvaluacionDTO dto = modelMapper.map(evaluacion, EvaluacionDTO.class);
        dto.setIdEstudiante(
                evaluacion.getEstudiante() != null ? evaluacion.getEstudiante().getId() : null
        );
        return dto;
    }
}
