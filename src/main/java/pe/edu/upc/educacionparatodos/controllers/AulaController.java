package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.AulaDTO;
import pe.edu.upc.educacionparatodos.dtos.AulaInsertDTO;
import pe.edu.upc.educacionparatodos.dtos.AulasPorColegioDTO;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.entities.Colegio;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IColegioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/aulas")
@SecurityRequirement(name = "bearerAuth")
public class AulaController {

    private final IAulaService aS;
    private final IColegioService cS;
    private final ModelMapper modelMapper;

    public AulaController(IAulaService aS, IColegioService cS, ModelMapper modelMapper) {
        this.aS = aS;
        this.cS = cS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<AulaDTO>> listar() {
        List<AulaDTO> lista = aS.list()
                .stream()
                .map(aula -> modelMapper.map(aula, AulaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AulaDTO> buscarId(@PathVariable Long id) {
        Aula aula = aS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un aula con el id: " + id));
        return ResponseEntity.ok(modelMapper.map(aula, AulaDTO.class));
    }

    @PostMapping
    public ResponseEntity<AulaDTO> registrar(@Valid @RequestBody AulaInsertDTO dto) {
        Colegio colegio = cS.listId(dto.getIdColegio())
                .orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + dto.getIdColegio()));

        Aula aula = modelMapper.map(dto, Aula.class);
        aula.setColegio(colegio);
        aula.setId(null);
        aS.insert(aula);

        AulaDTO response = modelMapper.map(aula, AulaDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(aula.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    public ResponseEntity<AulaDTO> actualizar(@Valid @RequestBody AulaDTO dto) {
        Aula existente = aS.listId(dto.getId()).orElseThrow(() -> new ResourceNotFoundException("No existe un aula con el id: " + dto.getId()));
        Colegio colegio = cS.listId(dto.getIdColegio()).orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + dto.getIdColegio()));

        existente.setGrado(dto.getGrado());
        existente.setSeccion(dto.getSeccion());
        existente.setEstado(dto.isEstado());
        existente.setColegio(colegio);

        aS.update(existente);
        return ResponseEntity.ok(modelMapper.map(existente, AulaDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Aula aula = aS.listId(id).orElseThrow(() -> new ResourceNotFoundException("No existe un aula con el id: " + id));
        aS.delete(aula.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cantidades")
    public ResponseEntity<List<AulasPorColegioDTO>> contarPorColegio() {
        List<AulasPorColegioDTO> lista = aS.contarAulasPorColegio()
                .stream()
                .map(item -> {
                    AulasPorColegioDTO dto = new AulasPorColegioDTO();
                    dto.setNombreColegio((String) item[0]);
                    dto.setCantidad(((Number) item[1]).longValue());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }
}
