export class AulaDTO {
  id: number = 0
  idColegio: number = 0
  grado: string = ''
  seccion: string = ''
  estado: boolean = false
  // Lo escribe el backend, se envia null
  fechaCreacion: string | null = null
  // Lo escribe el backend, se envia null
  actualizadoEn: string | null = null
}
