import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';

@Component({
  selector: 'app-evlistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './evlistcomponent.html',
  styleUrl: './evlistcomponent.css',
})
export class Evlistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'cEditar', 'cEliminar'];
  dataSource: EvaluacionDTO[] = [];
  mensaje: string = '';

  constructor(
    private eS: Evaluacionservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerEvaluaciones();
  }

  obtenerEvaluaciones(): void {
    this.eS.getEvaluacion().subscribe({
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

  eliminarEvaluacion(id: number): void {
    this.eS.deleteEvaluacion(id).subscribe({
      next: () => {
        this.obtenerEvaluaciones();
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
