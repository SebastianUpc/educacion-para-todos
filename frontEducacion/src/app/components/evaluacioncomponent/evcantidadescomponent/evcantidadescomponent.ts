import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { EvaluacionesPorEstudianteDTO } from '../../../models/EvaluacionesPorEstudianteDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';

@Component({
  selector: 'app-evcantidadescomponent',
  imports: [MatTableModule],
  templateUrl: './evcantidadescomponent.html',
  styleUrl: './evcantidadescomponent.css',
})
export class Evcantidadescomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2'];
  dataSource: EvaluacionesPorEstudianteDTO[] = [];
  mensaje: string = '';

  constructor(
    private eS: Evaluacionservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerCantidades();
  }

  obtenerCantidades(): void {
    this.eS.getCantidades().subscribe({
      next: (data) => {
        this.dataSource = data;
        this.mensaje = data.length === 0 ? 'No hay datos para mostrar.' : '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'No se pudo cargar el reporte.';
        this.cdr.detectChanges();
      },
    });
  }
}
