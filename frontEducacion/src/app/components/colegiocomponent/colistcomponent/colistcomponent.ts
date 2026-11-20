import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ColegioDTO } from '../../../models/ColegioDTO';
import { Colegioservice } from '../../../services/colegioservice';
import { Loginservice } from '../../../services/loginservice';

@Component({
  selector: 'app-colistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './colistcomponent.html',
  styleUrl: './colistcomponent.css',
})
export class Colistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4'];
  dataSource: ColegioDTO[] = [];
  mensaje: string = '';

  constructor(
    private lS: Loginservice,
    private cS: Colegioservice,
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
    const columnas: string[] = ['c1', 'c2', 'c3', 'c4'];
    if (this.esAdmin) {
      columnas.push('cEditar');
    }
    if (this.esAdmin) {
      columnas.push('cEliminar');
    }
    this.displayedColumns = columnas;
  }

  ngOnInit(): void {
    this.armarColumnas();
    this.obtenerColegios();
  }

  obtenerColegios(): void {
    this.cS.getColegio().subscribe({
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

  eliminarColegio(id: number): void {
    this.cS.deleteColegio(id).subscribe({
      next: () => {
        this.obtenerColegios();
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
