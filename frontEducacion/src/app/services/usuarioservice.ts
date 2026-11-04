import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UsuarioDTO } from '../models/UsuarioDTO';
import { UsuariosPorRolDTO } from '../models/UsuariosPorRolDTO';

@Injectable({
  providedIn: 'root',
})
export class Usuarioservice {
  private apiURL = `${environment.base}/api/usuarios`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getUsuario(): Observable<UsuarioDTO[]> {
    return this.http.get<UsuarioDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<UsuarioDTO> {
    return this.http.get<UsuarioDTO>(`${this.apiURL}/${id}`);
  }

  // Registrar
  insertUsuario(usuario: UsuarioDTO): Observable<UsuarioDTO> {
    return this.http.post<UsuarioDTO>(this.apiURL, usuario);
  }

  // Actualizar: el backend espera el id dentro del cuerpo
  update(dto: UsuarioDTO): Observable<UsuarioDTO> {
    return this.http.put<UsuarioDTO>(this.apiURL, dto);
  }

  // Eliminar
  deleteUsuario(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiURL}/${id}`);
  }

  // Buscar por estado: query simple con @RequestParam
  getEstado(estado: string): Observable<UsuarioDTO[]> {
    return this.http.get<UsuarioDTO[]>(`${this.apiURL}/estados`, {
      params: { estado: estado }
    });
  }

  // Query con JOIN: usuarios por rol
  getCantidades(): Observable<UsuariosPorRolDTO[]> {
    return this.http.get<UsuariosPorRolDTO[]>(`${this.apiURL}/cantidades`);
  }
}
