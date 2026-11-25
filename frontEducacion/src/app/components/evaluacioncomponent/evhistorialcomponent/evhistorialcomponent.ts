import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-evhistorialcomponent',
  imports: [MatTableModule, MatPaginatorModule, MatSelectModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './evhistorialcomponent.html',
  styleUrl: './evhistorialcomponent.css',
})
export class Evhistorialcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4'];
  dataSource = new MatTableDataSource<EvaluacionDTO>();

  // La tabla esta dentro de un @if, asi que el paginador aparece recien cuando
  // llegan datos: el setter lo engancha en ese momento y no en ngAfterViewInit.
  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator) {
    if (paginator) {
      this.dataSource.paginator = paginator;
    }
  }
  mensaje: string = '';

  estudianteSeleccionado: any = '';
  listaUsuario: UsuarioDTO[] = [];

  constructor(
    private eS: Evaluacionservice,
    private uS: Usuarioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // Cargar los estudiantes para el desplegable
    this.uS.getUsuario().subscribe({
      next: (data) => {
        this.listaUsuario = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
  }

  buscarPorEstudiante(): void {
    if (!this.estudianteSeleccionado) {
      this.dataSource.data = [];
      this.mensaje = 'Seleccione un estudiante.';
      return;
    }

    this.mensaje = 'Buscando...';
    this.dataSource.data = [];

    this.eS.getEstudiante(this.estudianteSeleccionado).subscribe({
      next: (data) => {
        this.dataSource.data = [...data];
        this.mensaje = data.length === 0
          ? 'Este estudiante todavia no ha rendido evaluaciones.'
          : '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.dataSource.data = [];
        this.mensaje = err?.error?.message || 'Error al consultar el historial.';
        this.cdr.detectChanges();
      },
    });
  }
}
