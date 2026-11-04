import { ChangeDetectorRef, Component } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule } from '@angular/material/button';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-usbuscarcomponent',
  imports: [MatTableModule, MatSelectModule, MatFormFieldModule, MatButtonModule],
  templateUrl: './usbuscarcomponent.html',
  styleUrl: './usbuscarcomponent.css',
})
export class Usbuscarcomponent {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6'];
  dataSource: UsuarioDTO[] = [];
  mensaje: string = '';
  estadoSeleccionado: string = '';

  opciones: any[] = [
    { value: 'ACTIVO', viewValue: 'ACTIVO' },
    { value: 'INACTIVO', viewValue: 'INACTIVO' },
  ];

  constructor(
    private uS: Usuarioservice,
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

    this.uS.getEstado(this.estadoSeleccionado).subscribe({
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
