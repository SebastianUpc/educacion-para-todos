import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-uslistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './uslistcomponent.html',
  styleUrl: './uslistcomponent.css',
})
export class Uslistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6', 'cEditar', 'cEliminar'];
  dataSource: UsuarioDTO[] = [];
  mensaje: string = '';

  constructor(
    private uS: Usuarioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerUsuarios();
  }

  obtenerUsuarios(): void {
    this.uS.getUsuario().subscribe({
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

  eliminarUsuario(id: number): void {
    this.uS.deleteUsuario(id).subscribe({
      next: () => {
        this.obtenerUsuarios();
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
