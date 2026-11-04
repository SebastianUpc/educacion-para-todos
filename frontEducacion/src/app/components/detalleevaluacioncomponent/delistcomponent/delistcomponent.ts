import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DetalleEvaluacionDTO } from '../../../models/DetalleEvaluacionDTO';
import { Detalleevaluacionservice } from '../../../services/detalleevaluacionservice';

@Component({
  selector: 'app-delistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule],
  templateUrl: './delistcomponent.html',
  styleUrl: './delistcomponent.css',
})
export class Delistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
  dataSource: DetalleEvaluacionDTO[] = [];
  mensaje: string = '';

  constructor(
    private dS: Detalleevaluacionservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerDetalles();
  }

  obtenerDetalles(): void {
    this.dS.getDetalleEvaluacion().subscribe({
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
}
