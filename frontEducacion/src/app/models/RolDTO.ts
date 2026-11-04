export class RolDTO {
  id: number = 0
  nombre: string = ''
  descripcion: string = ''
  // Lo escribe el backend, se envia null
  fechaCreacion: string | null = null
  // Lo escribe el backend, se envia null
  actualizadoEn: string | null = null
}
