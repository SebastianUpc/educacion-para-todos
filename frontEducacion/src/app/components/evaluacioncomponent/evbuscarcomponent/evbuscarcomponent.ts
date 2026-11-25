import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';

@Component({
  selector: 'app-evbuscarcomponent',
  imports: [MatTableModule, MatPaginatorModule, MatSelectModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './evbuscarcomponent.html',
  styleUrl: './evbuscarcomponent.css',
})
export class Evbuscarcomponent {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
  dataSource = new MatTableDataSource<EvaluacionDTO>();

  // La tabla esta dentro de un @if, asi que el paginador aparece recien cuando
  // llegan datos: el setter lo engancha en ese momento y no en ngAfterViewInit.
  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator) {
    if (paginator) {
      this.dataSource.paginator = paginator;
    }
  }
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
      this.dataSource.data = [];
      this.mensaje = 'Seleccione un estado.';
      return;
    }

    this.mensaje = 'Buscando...';
    this.dataSource.data = [];

    this.eS.getEstado(this.estadoSeleccionado).subscribe({
      next: (data) => {
        this.dataSource.data = [...data];
        this.mensaje = data.length === 0 ? 'No se encontraron registros.' : '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.dataSource.data = [];
        this.mensaje = 'Error al realizar la busqueda.';
        this.cdr.detectChanges();
      },
    });
  }
}
