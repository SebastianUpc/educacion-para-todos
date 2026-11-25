import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { DetalleEvaluacionDTO } from '../../../models/DetalleEvaluacionDTO';
import { Detalleevaluacionservice } from '../../../services/detalleevaluacionservice';

@Component({
  selector: 'app-delistcomponent',
  imports: [MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule],
  templateUrl: './delistcomponent.html',
  styleUrl: './delistcomponent.css',
})
export class Delistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5'];
  dataSource = new MatTableDataSource<DetalleEvaluacionDTO>();

  // La tabla esta dentro de un @if, asi que el paginador aparece recien cuando
  // llegan datos: el setter lo engancha en ese momento y no en ngAfterViewInit.
  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator) {
    if (paginator) {
      this.dataSource.paginator = paginator;
    }
  }
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
        this.dataSource.data = data;
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
