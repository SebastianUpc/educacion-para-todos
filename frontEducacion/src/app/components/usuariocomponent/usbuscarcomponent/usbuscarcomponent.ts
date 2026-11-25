import { ChangeDetectorRef, Component, ViewChild } from '@angular/core';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink } from '@angular/router';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';
import { Loginservice } from '../../../services/loginservice';

@Component({
  selector: 'app-usbuscarcomponent',
  imports: [MatTableModule, MatPaginatorModule, MatSelectModule, MatFormFieldModule, MatButtonModule, MatIconModule, RouterLink],
  templateUrl: './usbuscarcomponent.html',
  styleUrl: './usbuscarcomponent.css',
})
export class Usbuscarcomponent {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6'];
  dataSource = new MatTableDataSource<UsuarioDTO>();

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
    { value: 'ACTIVO', viewValue: 'ACTIVO' },
    { value: 'INACTIVO', viewValue: 'INACTIVO' },
  ];

  constructor(
    private lS: Loginservice,
    private uS: Usuarioservice,
    private cdr: ChangeDetectorRef
  ) {
    this.armarColumnas();
  }

  get gestiona(): boolean {
    return this.lS.hasAnyRole(['ADMIN', 'DOCENTE']);
  }

  // Desde aqui se llega al formulario de edicion, que es la unica forma de
  // reactivar a un usuario dado de baja: el listado principal no los muestra.
  armarColumnas(): void {
    const columnas: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6'];
    if (this.gestiona) {
      columnas.push('cEditar');
    }
    this.displayedColumns = columnas;
  }

  buscarPorEstado(): void {
    if (!this.estadoSeleccionado) {
      this.dataSource.data = [];
      this.mensaje = 'Seleccione un estado.';
      return;
    }

    this.mensaje = 'Buscando...';
    this.dataSource.data = [];

    this.uS.getEstado(this.estadoSeleccionado).subscribe({
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
