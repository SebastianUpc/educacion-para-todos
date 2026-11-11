import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { EjercicioDTO } from '../../../models/EjercicioDTO';
import { Ejercicioservice } from '../../../services/ejercicioservice';

@Component({
  selector: 'app-ejlistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './ejlistcomponent.html',
  styleUrl: './ejlistcomponent.css',
})
export class Ejlistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'cEditar', 'cEliminar'];
  dataSource: EjercicioDTO[] = [];
  mensaje: string = '';

  constructor(
    private eS: Ejercicioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerEjercicios();
  }

  obtenerEjercicios(): void {
    this.eS.getEjercicio().subscribe({
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

  eliminarEjercicio(id: number): void {
    this.eS.deleteEjercicio(id).subscribe({
      next: () => {
        this.obtenerEjercicios();
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
