export class EjercicioIaRequestDTO {
  idEstudiante: number = 0
  idTema: number = 0
  cantidad: number = 3
  // null = la API decide el nivel con el progreso y los resultados del estudiante
  nivelDificultad: number | null = null
  necesidades: string = ''
}
