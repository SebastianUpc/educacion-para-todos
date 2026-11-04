import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { RolDTO } from '../models/RolDTO';
import { RolInsertDTO } from '../models/RolInsertDTO';

@Injectable({
  providedIn: 'root',
})
export class Rolservice {
  private apiURL = `${environment.base}/api/roles`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getRol(): Observable<RolDTO[]> {
    return this.http.get<RolDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<RolDTO> {
    return this.http.get<RolDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertRol(rol: RolInsertDTO): Observable<RolInsertDTO> {
    return this.http.post<RolInsertDTO>(this.apiURL, rol);
  }

  // Actualizar: el backend espera el id dentro del cuerpo
  update(dto: RolDTO): Observable<RolDTO> {
    return this.http.put<RolDTO>(this.apiURL, dto);
  }

  // Eliminar
  deleteRol(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }
}
