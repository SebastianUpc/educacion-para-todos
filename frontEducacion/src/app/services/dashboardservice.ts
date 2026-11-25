import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { DashboardDTO } from '../models/DashboardDTO';

// HU-106. El aula no se envia: el backend la toma del usuario de la sesion.
@Injectable({
  providedIn: 'root',
})
export class Dashboardservice {
  private apiURL = `${environment.base}/api/dashboard`;

  constructor(private http: HttpClient) {}

  getDashboard(): Observable<DashboardDTO> {
    return this.http.get<DashboardDTO>(this.apiURL);
  }
}
