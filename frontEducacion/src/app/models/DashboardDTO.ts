import { EstudianteRendimientoDTO } from './EstudianteRendimientoDTO';
import { RendimientoTemaDTO } from './RendimientoTemaDTO';

export class DashboardDTO {
  alcance: string = ''
  hayDatos: boolean = false
  totalEstudiantes: number = 0
  totalEvaluaciones: number = 0
  evaluacionesFinalizadas: number = 0
  evaluacionesEnCurso: number = 0
  totalRespuestas: number = 0
  promedioAcademico: number | null = null
  porcentajeAciertos: number | null = null
  progresoPromedio: number | null = null
  notaMinimaAprobatoria: number = 11
  rendimientoPorTema: RendimientoTemaDTO[] = []
  estudiantesConDificultades: EstudianteRendimientoDTO[] = []
}
