import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { LoginRequest } from '../models/LoginRequest';
import { LoginResponse } from '../models/LoginResponse';

@Injectable({
  providedIn: 'root',
})
export class Loginservice {
  private apiURL = `${environment.base}/login`;

  constructor(private http: HttpClient) {}

  login(credenciales: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(this.apiURL, credenciales);
  }

  guardarSesion(data: LoginResponse): void {
    sessionStorage.setItem('token', data.token);
    sessionStorage.setItem('username', data.username);
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }

  getUsername(): string {
    return sessionStorage.getItem('username') || '';
  }

  estaLogueado(): boolean {
    return this.getToken() !== null;
  }

  salir(): void {
    sessionStorage.clear();
  }

  // Lee el claim "roles" del JWT que genera JwtTokenService en el backend.
  // Llega como texto separado por comas, por ejemplo: ROLE_ADMIN
  getRoles(): string[] {
    const token = this.getToken();
    if (!token) {
      return [];
    }
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const roles = payload.roles || '';
      return roles.split(',').filter((r: string) => r.length > 0);
    } catch {
      return [];
    }
  }

  tieneRol(rol: string): boolean {
    return this.getRoles().includes(rol) || this.getRoles().includes('ROLE_' + rol);
  }
}
