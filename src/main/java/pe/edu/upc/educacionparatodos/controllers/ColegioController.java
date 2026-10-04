package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.ColegioDTO;
import pe.edu.upc.educacionparatodos.dtos.ColegioInsertDTO;
import pe.edu.upc.educacionparatodos.dtos.ColegiosPorDistritoDTO;
import pe.edu.upc.educacionparatodos.entities.Colegio;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IColegioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/colegios")
@SecurityRequirement(name = "bearerAuth")
public class ColegioController {

    private final IColegioService cS;
    private final IAulaService aS;
    private final ModelMapper modelMapper;

    public ColegioController(IColegioService cS, IAulaService aS, ModelMapper modelMapper) {
        this.cS = cS;
        this.aS = aS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<ColegioDTO>> listar() {
        List<ColegioDTO> lista = cS.list()
                .stream()
                .map(col -> modelMapper.map(col, ColegioDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<ColegioDTO> buscarId(@PathVariable Long id) {
        Colegio colegio = cS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + id));
        return ResponseEntity.ok(modelMapper.map(colegio, ColegioDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Colegio colegio = cS.listId(id).orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + id));

        if (aS.existsByColegioId(colegio.getId())) {
            throw new BusinessRuleException(
                    "No se puede eliminar el colegio porque tiene aulas registradas"
            );
        }

        cS.delete(colegio.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColegioDTO> registrar(@Valid @RequestBody ColegioInsertDTO dto) {
        Colegio colegio = modelMapper.map(dto, Colegio.class);
        colegio.setId(null);
        cS.insert(colegio);
        ColegioDTO response = modelMapper.map(colegio, ColegioDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(colegio.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ColegioDTO> actualizar(@Valid @RequestBody ColegioDTO dto) {
        Colegio existente = cS.listId(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + dto.getId()));
        existente.setNombre(dto.getNombre());
        existente.setDireccion(dto.getDireccion());
        existente.setDistrito(dto.getDistrito());
        cS.update(existente);
        return ResponseEntity.ok(modelMapper.map(existente, ColegioDTO.class));
    }

    @GetMapping("/cantidades")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<List<ColegiosPorDistritoDTO>> contarPorDistrito() {
        List<ColegiosPorDistritoDTO> lista = cS.contarColegiosPorDistrito()
                .stream()
                .map(item -> {
                    ColegiosPorDistritoDTO dto = new ColegiosPorDistritoDTO();
                    dto.setDistrito((String) item[0]);
                    dto.setCantidad(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

}
