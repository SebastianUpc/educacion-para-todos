import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { ColegioDTO } from '../../../models/ColegioDTO';
import { Colegioservice } from '../../../services/colegioservice';

@Component({
  selector: 'app-colistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './colistcomponent.html',
  styleUrl: './colistcomponent.css',
})
export class Colistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'cEditar', 'cEliminar'];
  dataSource: ColegioDTO[] = [];
  mensaje: string = '';

  constructor(
    private cS: Colegioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
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
