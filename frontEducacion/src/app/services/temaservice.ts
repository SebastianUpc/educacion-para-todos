import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { TemaDTO } from '../models/TemaDTO';
import { TemaInsertDTO } from '../models/TemaInsertDTO';

@Injectable({
  providedIn: 'root',
})
export class Temaservice {
  private apiURL = `${environment.base}/api/temas`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getTema(): Observable<TemaDTO[]> {
    return this.http.get<TemaDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<TemaDTO> {
    return this.http.get<TemaDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertTema(tema: TemaInsertDTO): Observable<TemaInsertDTO> {
    return this.http.post<TemaInsertDTO>(this.apiURL, tema);
  }

  // Actualizar: el backend espera el id en la URL
  update(id: number, dto: TemaInsertDTO): Observable<TemaInsertDTO> {
    return this.http.put<TemaInsertDTO>(`${this.apiURL}/${id}`, dto);
  }

  // Eliminar
  deleteTema(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
}
