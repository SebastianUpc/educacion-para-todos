import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DetalleEvaluacionDTO } from '../models/DetalleEvaluacionDTO';

@Injectable({
  providedIn: 'root',
})
export class Detalleevaluacionservice {
  private apiURL = `${environment.base}/api/detalles-evaluacion`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getDetalleEvaluacion(): Observable<DetalleEvaluacionDTO[]> {
    return this.http.get<DetalleEvaluacionDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<DetalleEvaluacionDTO> {
    return this.http.get<DetalleEvaluacionDTO>(`${this.apiURL}/${id}`);
  }
}
