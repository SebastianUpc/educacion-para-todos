import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AulaDTO } from '../models/AulaDTO';
import { AulaInsertDTO } from '../models/AulaInsertDTO';
import { AulasPorColegioDTO } from '../models/AulasPorColegioDTO';

@Injectable({
  providedIn: 'root',
})
export class Aulaservice {
  private apiURL = `${environment.base}/api/aulas`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getAula(): Observable<AulaDTO[]> {
    return this.http.get<AulaDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<AulaDTO> {
    return this.http.get<AulaDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertAula(aula: AulaInsertDTO): Observable<AulaInsertDTO> {
    return this.http.post<AulaInsertDTO>(this.apiURL, aula);
  }

  // Actualizar: el backend espera el id dentro del cuerpo
  update(dto: AulaDTO): Observable<AulaDTO> {
    return this.http.put<AulaDTO>(this.apiURL, dto);
  }

  // Eliminar
  deleteAula(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  // Query con JOIN: aulas por colegio
  getCantidades(): Observable<AulasPorColegioDTO[]> {
    return this.http.get<AulasPorColegioDTO[]>(`${this.apiURL}/cantidades`);
  }
}
