package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.TemaDTO;
import pe.edu.upc.educacionparatodos.dtos.TemaInsertDTO;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.servicesinterfaces.ITemaService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/temas")
@SecurityRequirement(name = "bearerAuth")
public class TemaController {

    private final ITemaService tS;
    private final ModelMapper modelMapper;

    public TemaController(ITemaService tS, ModelMapper modelMapper) {
        this.tS = tS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<TemaDTO>> listar() {
        List<TemaDTO> lista = tS.list()
                .stream()
                .map(tema -> modelMapper.map(tema, TemaDTO.class))
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<TemaDTO> buscarId(@PathVariable Long id) {
        return ResponseEntity.ok(modelMapper.map(tS.findById(id), TemaDTO.class));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<TemaDTO> registrar(@Valid @RequestBody TemaInsertDTO dto) {
        Tema tema = modelMapper.map(dto, Tema.class);
        tema.setId(null);
        Tema guardado = tS.insert(tema);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity.created(location).body(modelMapper.map(guardado, TemaDTO.class));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<TemaDTO> actualizar(@PathVariable Long id, @Valid @RequestBody TemaInsertDTO dto) {
        Tema actualizado = tS.update(id, modelMapper.map(dto, Tema.class));
        return ResponseEntity.ok(modelMapper.map(actualizado, TemaDTO.class));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tS.delete(id);
        return ResponseEntity.noContent().build();
    }
}
