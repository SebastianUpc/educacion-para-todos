package pe.edu.upc.educacionparatodos.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.ColegioDTO;
import pe.edu.upc.educacionparatodos.dtos.ColegioInsertDTO;
import pe.edu.upc.educacionparatodos.entities.Colegio;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IColegioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/colegios")
public class ColegioController {

    private final IColegioService cS;
    private final ModelMapper modelMapper;

    public ColegioController(IColegioService cS, ModelMapper modelMapper) {
        this.cS = cS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<ColegioDTO>> listar() {
        List<ColegioDTO> lista = cS.list()
                .stream()
                .map(col -> modelMapper.map(col, ColegioDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColegioDTO> buscarId(@PathVariable Long id) {
        Colegio colegio = cS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + id));
        return ResponseEntity.ok(modelMapper.map(colegio, ColegioDTO.class));
    }

    @PostMapping
    public ResponseEntity<ColegioDTO> registrar(@Valid @RequestBody ColegioInsertDTO dto) {
        Colegio colegio = modelMapper.map(dto, Colegio.class);
        colegio.setIdColegio(null);
        cS.insert(colegio);
        ColegioDTO response = modelMapper.map(colegio, ColegioDTO.class);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(colegio.getIdColegio()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping
    public ResponseEntity<ColegioDTO> actualizar(@Valid @RequestBody ColegioDTO dto) {
        Colegio existente = cS.listId(dto.getIdColegio()).orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + dto.getIdColegio()));
        existente.setNombreColegio(dto.getNombreColegio());
        existente.setDireccionColegio(dto.getDireccionColegio());
        existente.setDistritoColegio(dto.getDistritoColegio());
        cS.update(existente);
        return ResponseEntity.ok(modelMapper.map(existente, ColegioDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Colegio colegio = cS.listId(id).orElseThrow(() -> new ResourceNotFoundException("No existe un colegio con el id: " + id));
        cS.delete(colegio.getIdColegio());
        return ResponseEntity.noContent().build();
    }
}
