import { ChangeDetectorRef, Component } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';

@Component({
  selector: 'app-evbuscarcomponent',
  imports: [MatTableModule, MatSelectModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './evbuscarcomponent.html',
  styleUrl: './evbuscarcomponent.css',
})
export class Evbuscarcomponent {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
  dataSource: EvaluacionDTO[] = [];
  mensaje: string = '';
  estadoSeleccionado: string = '';

  opciones: any[] = [
    { value: 'EN_CURSO', viewValue: 'EN_CURSO' },
    { value: 'FINALIZADA', viewValue: 'FINALIZADA' },
  ];

  constructor(
    private eS: Evaluacionservice,
    private cdr: ChangeDetectorRef
  ) {}

  buscarPorEstado(): void {
    if (!this.estadoSeleccionado) {
      this.dataSource = [];
      this.mensaje = 'Seleccione un estado.';
      return;
    }

    this.mensaje = 'Buscando...';
    this.dataSource = [];

    this.eS.getEstado(this.estadoSeleccionado).subscribe({
      next: (data) => {
        this.dataSource = [...data];
        this.mensaje = data.length === 0 ? 'No se encontraron registros.' : '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.dataSource = [];
        this.mensaje = 'Error al realizar la busqueda.';
        this.cdr.detectChanges();
      },
    });
  }
}
