import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterLink } from '@angular/router';
import { AulaDTO } from '../../../models/AulaDTO';
import { Aulaservice } from '../../../services/aulaservice';

@Component({
  selector: 'app-aulistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule, RouterLink],
  templateUrl: './aulistcomponent.html',
  styleUrl: './aulistcomponent.css',
})
export class Aulistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'cEditar', 'cEliminar'];
  dataSource: AulaDTO[] = [];
  mensaje: string = '';

  constructor(
    private aS: Aulaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerAulas();
  }

  obtenerAulas(): void {
    this.aS.getAula().subscribe({
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

  eliminarAula(id: number): void {
    this.aS.deleteAula(id).subscribe({
      next: () => {
        this.obtenerAulas();
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
