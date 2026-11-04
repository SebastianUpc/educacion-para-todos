import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ProgresoDTO } from '../models/ProgresoDTO';

@Injectable({
  providedIn: 'root',
})
export class Progresoservice {
  private apiURL = `${environment.base}/api/progresos`;

  constructor(private http: HttpClient) {}

  // Listar todos los registros
  getProgreso(): Observable<ProgresoDTO[]> {
    return this.http.get<ProgresoDTO[]>(this.apiURL);
  }

  // Buscar un registro por su id: lo usa el formulario de actualizacion
  listId(id: number): Observable<ProgresoDTO> {
    return this.http.get<ProgresoDTO>(`${this.apiURL}/${id}`);
  }
}
