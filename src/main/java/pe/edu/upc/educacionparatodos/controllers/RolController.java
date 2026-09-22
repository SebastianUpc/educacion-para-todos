package pe.edu.upc.educacionparatodos.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.RolDTO;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IRolService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final IRolService rS;
    private final IUsuarioService uS;
    private final ModelMapper modelMapper;

    public RolController(IRolService rS, IUsuarioService uS, ModelMapper modelMapper) {
        this.rS = rS;
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<RolDTO>> listar() {
        List<RolDTO> lista = rS.list()
                .stream()
                .map(rol -> modelMapper.map(rol, RolDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> buscarId(@PathVariable Long id) {
        Rol rol = rS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un rol con el id: " + id
                ));

        return ResponseEntity.ok(modelMapper.map(rol, RolDTO.class));
    }

    @PostMapping
    public ResponseEntity<RolDTO> registrar(@Valid @RequestBody pe.edu.upc.educacionparatodos.dtos.RolInsertDTO dto) {
        if (rS.existsByNombre(dto.getNombre())) {
            throw new BusinessRuleException(
                    "Ya existe un rol con el nombre: " + dto.getNombre()
            );
        }

        Rol rol = modelMapper.map(dto, Rol.class);
        rol.setId(null);
        rol.setDescripcionRol(dto.getDescripcion());

        rS.insert(rol);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(rol.getId())
                .toUri();

        return ResponseEntity.created(location).body(modelMapper.map(rol, pe.edu.upc.educacionparatodos.dtos.RolDTO.class));
    }

    @PutMapping
    public ResponseEntity<RolDTO> actualizar(@Valid @RequestBody RolDTO dto) {
        Rol rol = rS.listId(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un rol con el id: " + dto.getId()
                ));

        boolean cambioDeNombre = !rol.getNombre().equals(dto.getNombre());
        if (cambioDeNombre && rS.existsByNombre(dto.getNombre())) {
            throw new BusinessRuleException(
                    "Ya existe otro rol con el nombre: " + dto.getNombre()
            );
        }

        rol.setNombre(dto.getNombre());
        rol.setDescripcionRol(dto.getDescripcion());

        rS.update(rol);

        return ResponseEntity.ok(modelMapper.map(rol, RolDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Rol rol = rS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un rol con el id: " + id
                ));

        if (uS.existsByRolId(rol.getId())) {
            throw new BusinessRuleException(
                    "No se puede eliminar el rol porque tiene usuarios asignados"
            );
        }

        rS.delete(rol.getId());

        return ResponseEntity.noContent().build();
    }
}
