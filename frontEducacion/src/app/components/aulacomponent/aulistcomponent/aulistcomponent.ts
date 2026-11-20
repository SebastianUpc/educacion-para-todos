import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { AulaDTO } from '../../../models/AulaDTO';
import { Aulaservice } from '../../../services/aulaservice';
import { Loginservice } from '../../../services/loginservice';

@Component({
  selector: 'app-aulistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './aulistcomponent.html',
  styleUrl: './aulistcomponent.css',
})
export class Aulistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
  dataSource: AulaDTO[] = [];
  mensaje: string = '';

  constructor(
    private lS: Loginservice,
    private aS: Aulaservice,
    private cdr: ChangeDetectorRef
  ) {}

  get esAdmin(): boolean {
    return this.lS.hasAnyRole(['ADMIN']);
  }

  get gestiona(): boolean {
    return this.lS.hasAnyRole(['ADMIN', 'DOCENTE']);
  }

  // Las columnas de accion solo se muestran a quien la API le permite esa operacion.
  // Ocultarlas no reemplaza la validacion del backend: evita intentos que darian 403.
  armarColumnas(): void {
    const columnas: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
    if (this.gestiona) {
      columnas.push('cEditar');
    }
    if (this.esAdmin) {
      columnas.push('cEliminar');
    }
    this.displayedColumns = columnas;
  }

  ngOnInit(): void {
    this.armarColumnas();
    this.obtenerAulas();
  }

  obtenerAulas(): void {
    this.aS.getAula().subscribe({
      next: (data) => {
        this.dataSource = data;
        this.mensaje = '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'No se pudo cargar la lista. Revise que haya iniciado sesion.';
        this.cdr.detectChanges();
      },
    });
  }

  eliminarAula(id: number): void {
    this.aS.deleteAula(id).subscribe({
      next: () => {
        this.obtenerAulas();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = err?.error?.message || 'No se pudo eliminar el registro.';
        this.cdr.detectChanges();
      },
    });
  }
}
