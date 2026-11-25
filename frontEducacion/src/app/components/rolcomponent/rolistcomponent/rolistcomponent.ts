import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { RolDTO } from '../../../models/RolDTO';
import { Rolservice } from '../../../services/rolservice';

@Component({
  selector: 'app-rolistcomponent',
  imports: [MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './rolistcomponent.html',
  styleUrl: './rolistcomponent.css',
})
export class Rolistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'cEditar', 'cEliminar'];
  dataSource = new MatTableDataSource<RolDTO>();

  // La tabla esta dentro de un @if, asi que el paginador aparece recien cuando
  // llegan datos: el setter lo engancha en ese momento y no en ngAfterViewInit.
  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator) {
    if (paginator) {
      this.dataSource.paginator = paginator;
    }
  }
  mensaje: string = '';

  constructor(
    private rS: Rolservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerRoles();
  }

  obtenerRoles(): void {
    this.rS.getRol().subscribe({
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

  eliminarRol(id: number): void {
    this.rS.deleteRol(id).subscribe({
      next: () => {
        this.obtenerRoles();
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
