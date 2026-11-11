import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { TemaDTO } from '../../../models/TemaDTO';
import { Temaservice } from '../../../services/temaservice';

@Component({
  selector: 'app-telistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './telistcomponent.html',
  styleUrl: './telistcomponent.css',
})
export class Telistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'cEditar', 'cEliminar'];
  dataSource: TemaDTO[] = [];
  mensaje: string = '';

  constructor(
    private tS: Temaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerTemas();
  }

  obtenerTemas(): void {
    this.tS.getTema().subscribe({
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

  eliminarTema(id: number): void {
    this.tS.deleteTema(id).subscribe({
      next: () => {
        this.obtenerTemas();
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
