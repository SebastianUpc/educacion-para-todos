import { EjercicioInsertDTO } from './EjercicioInsertDTO';

export class EjercicioIaResponseDTO {
  idEstudiante: number = 0
  nombreEstudiante: string = ''
  idTema: number = 0
  nombreTema: string = ''
  nivelAplicado: number = 0
  contextoUsado: string[] = []
  ejercicios: EjercicioInsertDTO[] = []
}
