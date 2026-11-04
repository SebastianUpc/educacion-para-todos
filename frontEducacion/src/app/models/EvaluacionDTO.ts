export class EvaluacionDTO {
  id: number = 0
  idEstudiante: number = 0
  estado: string = ''
  // Lo escribe el backend, se envia null
  fechaInicio: string | null = null
  // Lo escribe el backend, se envia null
  fechaFin: string | null = null
}
