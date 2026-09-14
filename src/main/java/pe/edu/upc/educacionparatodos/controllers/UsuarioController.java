package pe.edu.upc.educacionparatodos.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.educacionparatodos.dtos.UsuarioDTO;
import pe.edu.upc.educacionparatodos.entities.Aula;
import pe.edu.upc.educacionparatodos.entities.Rol;
import pe.edu.upc.educacionparatodos.entities.Usuario;
import pe.edu.upc.educacionparatodos.exceptions.BusinessRuleException;
import pe.edu.upc.educacionparatodos.exceptions.ResourceNotFoundException;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IAulaService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IRolService;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IUsuarioService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";

    private final IUsuarioService uS;
    private final IRolService rS;
    private final IAulaService aS;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(IUsuarioService uS, IRolService rS, IAulaService aS,
                             ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.uS = uS;
        this.rS = rS;
        this.aS = aS;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listar() {
        List<UsuarioDTO> lista = uS.list()
                .stream()
                .filter(u -> !ESTADO_INACTIVO.equals(u.getEstado()))
                .map(this::convertirADTO)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarId(@PathVariable Long id) {
        Usuario usuario = uS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con el id: " + id
                ));

        return ResponseEntity.ok(convertirADTO(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> registrar(@Valid @RequestBody UsuarioDTO dto) {
        if (uS.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException(
                    "Ya existe un usuario registrado con el correo: " + dto.getEmail()
            );
        }

        Usuario usuario = modelMapper.map(dto, Usuario.class);
        usuario.setId(null);
        usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(buscarRol(dto.getIdRol()));
        usuario.setAula(buscarAula(dto.getIdAula()));
        usuario.setApoderado(buscarApoderado(dto.getIdApoderado()));
        usuario.setEstado(ESTADO_ACTIVO);

        uS.insert(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(convertirADTO(usuario));
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> actualizar(@Valid @RequestBody UsuarioDTO dto) {
        Usuario usuario = uS.listId(dto.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con el id: " + dto.getId()
                ));

        boolean cambioDeCorreo = !usuario.getEmail().equals(dto.getEmail());
        if (cambioDeCorreo && uS.existsByEmail(dto.getEmail())) {
            throw new BusinessRuleException(
                    "El correo ya pertenece a otro usuario: " + dto.getEmail()
            );
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(buscarRol(dto.getIdRol()));
        usuario.setAula(buscarAula(dto.getIdAula()));
        usuario.setApoderado(buscarApoderado(dto.getIdApoderado()));

        uS.update(usuario);

        return ResponseEntity.ok(convertirADTO(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Usuario usuario = uS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un usuario con el id: " + id
                ));

        usuario.setEstado(ESTADO_INACTIVO);
        uS.update(usuario);

        return ResponseEntity.noContent().build();
    }

    private Rol buscarRol(Long idRol) {
        return rS.listId(idRol)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un rol con el id: " + idRol
                ));
    }

    private Aula buscarAula(Long idAula) {
        if (idAula == null) {
            return null;
        }
        return aS.listId(idAula)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un aula con el id: " + idAula
                ));
    }

    private Usuario buscarApoderado(Long idApoderado) {
        if (idApoderado == null) {
            return null;
        }
        return uS.listId(idApoderado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un apoderado con el id: " + idApoderado
                ));
    }

    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = modelMapper.map(usuario, UsuarioDTO.class);
        dto.setPassword(null);
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getId() : null);
        dto.setIdAula(usuario.getAula() != null ? usuario.getAula().getId() : null);
        dto.setIdApoderado(usuario.getApoderado() != null ? usuario.getApoderado().getId() : null);
        return dto;
    }
}
