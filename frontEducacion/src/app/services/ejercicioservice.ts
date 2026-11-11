import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { EjercicioDTO } from '../models/EjercicioDTO';
import { EjercicioInsertDTO } from '../models/EjercicioInsertDTO';

@Injectable({
  providedIn: 'root',
})
export class Ejercicioservice {
  private apiURL = `${environment.base}/api/ejercicios`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getEjercicio(): Observable<EjercicioDTO[]> {
    return this.http.get<EjercicioDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<EjercicioDTO> {
    return this.http.get<EjercicioDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertEjercicio(ejercicio: EjercicioInsertDTO): Observable<EjercicioInsertDTO> {
    return this.http.post<EjercicioInsertDTO>(this.apiURL, ejercicio);
  }

  // Actualizar: el backend espera el id en la URL
  update(id: number, dto: EjercicioInsertDTO): Observable<EjercicioInsertDTO> {
    return this.http.put<EjercicioInsertDTO>(`${this.apiURL}/${id}`, dto);
  }

  // Eliminar
  deleteEjercicio(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
}
