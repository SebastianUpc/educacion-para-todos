export class EstudianteRendimientoDTO {
  idEstudiante: number = 0
  nombreCompleto: string = ''
  aula: string | null = null
  totalRespuestas: number = 0
  promedio: number | null = null
  porcentajeAciertos: number | null = null
  progresoPromedio: number | null = null
  temaMasDebil: string | null = null
  motivos: string[] = []
}
