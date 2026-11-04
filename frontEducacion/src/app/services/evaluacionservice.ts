import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EvaluacionDTO } from '../models/EvaluacionDTO';
import { EvaluacionesPorEstudianteDTO } from '../models/EvaluacionesPorEstudianteDTO';

@Injectable({
  providedIn: 'root',
})
export class Evaluacionservice {
  private apiURL = `${environment.base}/api/evaluaciones`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getEvaluacion(): Observable<EvaluacionDTO[]> {
    return this.http.get<EvaluacionDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<EvaluacionDTO> {
    return this.http.get<EvaluacionDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertEvaluacion(evaluacion: EvaluacionDTO): Observable<EvaluacionDTO> {
    return this.http.post<EvaluacionDTO>(this.apiURL, evaluacion);
  }

  // Actualizar: el backend espera el id dentro del cuerpo
  update(dto: EvaluacionDTO): Observable<EvaluacionDTO> {
    return this.http.put<EvaluacionDTO>(this.apiURL, dto);
  }

  // Eliminar
  deleteEvaluacion(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  // Buscar por estado: query simple con @RequestParam
  getEstado(estado: string): Observable<EvaluacionDTO[]> {
    return this.http.get<EvaluacionDTO[]>(`${this.apiURL}/estados`, {
      params: { estado: estado }
    });
  }

  // Query con JOIN: evaluaciones por estudiante
  getCantidades(): Observable<EvaluacionesPorEstudianteDTO[]> {
    return this.http.get<EvaluacionesPorEstudianteDTO[]>(`${this.apiURL}/cantidades`);
  }
}
