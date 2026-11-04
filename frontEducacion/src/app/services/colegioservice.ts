import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ColegioDTO } from '../models/ColegioDTO';
import { ColegioInsertDTO } from '../models/ColegioInsertDTO';
import { ColegiosPorDistritoDTO } from '../models/ColegiosPorDistritoDTO';

@Injectable({
  providedIn: 'root',
})
export class Colegioservice {
  private apiURL = `${environment.base}/api/colegios`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getColegio(): Observable<ColegioDTO[]> {
    return this.http.get<ColegioDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<ColegioDTO> {
    return this.http.get<ColegioDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertColegio(colegio: ColegioInsertDTO): Observable<ColegioInsertDTO> {
    return this.http.post<ColegioInsertDTO>(this.apiURL, colegio);
  }

  // Actualizar: el backend espera el id dentro del cuerpo
  update(dto: ColegioDTO): Observable<ColegioDTO> {
    return this.http.put<ColegioDTO>(this.apiURL, dto);
  }

  // Eliminar
  deleteColegio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  // Query con JOIN: colegios por distrito
  getCantidades(): Observable<ColegiosPorDistritoDTO[]> {
    return this.http.get<ColegiosPorDistritoDTO[]>(`${this.apiURL}/cantidades`);
  }
}
