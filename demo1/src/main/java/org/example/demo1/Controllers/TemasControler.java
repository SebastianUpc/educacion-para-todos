package org.example.demo1.Controllers;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.example.demo1.DTO.TemasDto;
import org.example.demo1.Entities.Temas;
import org.example.demo1.ServicesImplements.TemasServiceImplmentd;
import org.example.demo1.ServicesInterfaces.ITemasServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/temas")
public class TemasControler {
    @Autowired
    private ITemasServices tS;

    @GetMapping
    public ResponseEntity<List<TemasDto>> Listar(){
        ModelMapper m = new ModelMapper();
        List<TemasDto> ListarTemas = tS.list().stream()
                .map(x->m.map(x, TemasDto.class)).collect(Collectors.toList());
        return ResponseEntity.ok(ListarTemas);
    }

    @PostMapping("/nuevo")
    public ResponseEntity<?> registrar(@RequestBody TemasDto dto){
        ModelMapper m = new ModelMapper();
        Temas c = m.map(dto, Temas.class);

        Temas com = tS.insert(c);
        TemasDto responseDTO = m.map(com, TemasDto.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/actualiza")
    public ResponseEntity<String> actualizar(@RequestBody TemasDto dto) {

        Optional<Temas> existente = tS.listid(dto.getId_temas());
        if (existente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Tema no encontrado");
        }

        Temas com = existente.get();

        com.setNombreT(dto.getNombreT());
        com.setMateria(dto.getMateria());
        tS.update(com);

        return ResponseEntity.ok("Comunidad actualizado correctamente");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable int id) {
        Optional<Temas> temas = tS.listid(id);

        if (temas.isPresent()) {
            tS.delete(id);
            return ResponseEntity.ok("Tema eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("machine no encontrado");
        }
    }
}
