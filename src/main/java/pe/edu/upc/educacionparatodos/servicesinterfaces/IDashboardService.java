package pe.edu.upc.educacionparatodos.servicesinterfaces;

import pe.edu.upc.educacionparatodos.dtos.DashboardDTO;

public interface IDashboardService {

    // Indicadores de los estudiantes visibles para el usuario de la sesion
    DashboardDTO obtenerDashboard();
}
