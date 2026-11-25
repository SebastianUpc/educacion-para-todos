import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EjercicioDTO } from '../models/EjercicioDTO';
import { EjercicioInsertDTO } from '../models/EjercicioInsertDTO';
import { EjercicioIaRequestDTO } from '../models/EjercicioIaRequestDTO';
import { EjercicioIaResponseDTO } from '../models/EjercicioIaResponseDTO';
import { EstudianteResumenDTO } from '../models/EstudianteResumenDTO';

// HU-105. El frontend nunca habla con la IA ni conoce la API key: solo llama al
// backend, que es quien tiene la credencial (CA09).
@Injectable({
  providedIn: 'root',
})
export class Ejercicioiaservice {
  private apiURL = `${environment.base}/api/ejercicios-ia`;

  constructor(private http: HttpClient) {}

  // Estudiantes del aula del docente (ADMIN recibe todos)
  getEstudiantes(): Observable<EstudianteResumenDTO[]> {
    return this.http.get<EstudianteResumenDTO[]>(`${this.apiURL}/estudiantes`);
  }

  // Pide las propuestas a la IA: todavia no se guardan
  generar(dto: EjercicioIaRequestDTO): Observable<EjercicioIaResponseDTO> {
    return this.http.post<EjercicioIaResponseDTO>(`${this.apiURL}/generar`, dto);
  }

  // Guarda solo los ejercicios que el docente aprobo
  guardar(ejercicios: EjercicioInsertDTO[]): Observable<EjercicioDTO[]> {
    return this.http.post<EjercicioDTO[]>(`${this.apiURL}/guardar`, { ejercicios });
  }
}
