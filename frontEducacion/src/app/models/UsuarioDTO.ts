export class UsuarioDTO {
  id: number = 0
  nombre: string = ''
  apellido: string = ''
  email: string = ''
  password: string = ''
  idRol: number = 0
  // Campo opcional: si no se elige nada se envia null
  idAula: number | null = null
  // Campo opcional: si no se elige nada se envia null
  idApoderado: number | null = null
  estado: string = ''
}
