import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { ProgresoDTO } from '../../../models/ProgresoDTO';
import { Progresoservice } from '../../../services/progresoservice';

@Component({
  selector: 'app-prlistcomponent',
  imports: [MatTableModule, MatIconModule, MatButtonModule],
  templateUrl: './prlistcomponent.html',
  styleUrl: './prlistcomponent.css',
})
export class Prlistcomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2', 'c3', 'c4', 'c5', 'c6'];
  dataSource: ProgresoDTO[] = [];
  mensaje: string = '';

  constructor(
    private pS: Progresoservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerProgresos();
  }

  obtenerProgresos(): void {
    this.pS.getProgreso().subscribe({
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
}
