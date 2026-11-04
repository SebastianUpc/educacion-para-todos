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
    return this.lS.estaLogueado();
  }

  salir(): void {
    this.lS.salir();
    this.router.navigate(['/login']);
  }
}
