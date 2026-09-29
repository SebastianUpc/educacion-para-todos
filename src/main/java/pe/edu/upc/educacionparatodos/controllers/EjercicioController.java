package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.EjercicioDTO;
import pe.edu.upc.educacionparatodos.dtos.EjercicioInsertDTO;
import pe.edu.upc.educacionparatodos.entities.Ejercicio;
import pe.edu.upc.educacionparatodos.entities.Tema;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IEjercicioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/ejercicios")
@SecurityRequirement(name = "bearerAuth")
public class EjercicioController {

    private final IEjercicioService eS;
    private final ModelMapper modelMapper;

    public EjercicioController(IEjercicioService eS, ModelMapper modelMapper) {
        this.eS = eS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<List<EjercicioDTO>> listar() {
        List<EjercicioDTO> lista = eS.list()
                .stream()
                .map(this::convertirADTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE','ESTUDIANTE','APODERADO')")
    public ResponseEntity<EjercicioDTO> buscarId(@PathVariable Long id) {
        return ResponseEntity.ok(convertirADTO(eS.findById(id)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<EjercicioDTO> registrar(@Valid @RequestBody EjercicioInsertDTO dto) {
        Ejercicio guardado = eS.insert(convertirAEntidad(dto));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(guardado.getId())
                .toUri();

        return ResponseEntity.created(location).body(convertirADTO(guardado));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<EjercicioDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EjercicioInsertDTO dto) {
        Ejercicio actualizado = eS.update(id, convertirAEntidad(dto));
        return ResponseEntity.ok(convertirADTO(actualizado));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eS.delete(id);
        return ResponseEntity.noContent().build();
    }

    // El servicio reemplaza este Tema (solo con id) por el real, o lanza 404 si no existe
    private Ejercicio convertirAEntidad(EjercicioInsertDTO dto) {
        Ejercicio ejercicio = modelMapper.map(dto, Ejercicio.class);
        ejercicio.setId(null);

        Tema tema = new Tema();
        tema.setId(dto.getIdTema());
        ejercicio.setTema(tema);

        return ejercicio;
    }

    private EjercicioDTO convertirADTO(Ejercicio ejercicio) {
        EjercicioDTO dto = modelMapper.map(ejercicio, EjercicioDTO.class);
        dto.setIdTema(ejercicio.getTema() != null ? ejercicio.getTema().getId() : null);
        dto.setNombreTema(ejercicio.getTema() != null ? ejercicio.getTema().getNombre() : null);
        return dto;
    }
}
