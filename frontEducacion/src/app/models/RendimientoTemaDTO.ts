export class RendimientoTemaDTO {
  idTema: number = 0
  tema: string = ''
  totalRespuestas: number = 0
  respuestasCorrectas: number = 0
  porcentajeAciertos: number | null = null
  promedio: number | null = null
}
