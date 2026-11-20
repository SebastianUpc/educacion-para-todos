import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Loginservice } from '../../services/loginservice';

@Component({
  selector: 'app-menucomponent',
  imports: [MatToolbarModule, MatButtonModule, MatIconModule, MatMenuModule, RouterLink],
  templateUrl: './menucomponent.html',
  styleUrl: './menucomponent.css',
})
export class Menucomponent {
  constructor(
    private lS: Loginservice,
    private router: Router
  ) {}

  get usuario(): string {
    return this.lS.getUsername();
  }

  get logueado(): boolean {
    return this.lS.isAuthenticated();
  }

  // El menu muestra solo lo que el perfil puede ejecutar segun los @PreAuthorize
  // de la API. Ocultar una opcion no reemplaza esa validacion: el backend sigue
  // siendo quien autoriza. Esto solo evita que el usuario intente algo que
  // terminaria en un 403.
  get esAdmin(): boolean {
    return this.lS.hasAnyRole(['ADMIN']);
  }

  get gestiona(): boolean {
    return this.lS.hasAnyRole(['ADMIN', 'DOCENTE']);
  }

  salir(): void {
    this.lS.logout();
    this.router.navigate(['/login']);
  }
}
