package pe.edu.upc.educacionparatodos.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.educacionparatodos.dtos.DashboardDTO;
import pe.edu.upc.educacionparatodos.servicesinterfaces.IDashboardService;

// HU-106: dashboard de seguimiento academico.
// No recibe el aula por parametro: se toma del usuario de la sesion, asi un
// docente no puede pedir datos de un aula que no es suya (CA02).
@RestController
@RequestMapping("/api/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final IDashboardService dS;

    public DashboardController(IDashboardService dS) {
        this.dS = dS;
    }

    @Operation(summary = "Indicadores académicos de los estudiantes del aula del docente (ADMIN ve todas)")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCENTE')")
    public ResponseEntity<DashboardDTO> obtener() {
        return ResponseEntity.ok(dS.obtenerDashboard());
    }
}
