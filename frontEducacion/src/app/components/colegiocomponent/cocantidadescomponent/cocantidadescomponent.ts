import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { ColegiosPorDistritoDTO } from '../../../models/ColegiosPorDistritoDTO';
import { Colegioservice } from '../../../services/colegioservice';

@Component({
  selector: 'app-cocantidadescomponent',
  imports: [MatTableModule],
  templateUrl: './cocantidadescomponent.html',
  styleUrl: './cocantidadescomponent.css',
})
export class Cocantidadescomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2'];
  dataSource: ColegiosPorDistritoDTO[] = [];
  mensaje: string = '';

  constructor(
    private cS: Colegioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerCantidades();
  }

  obtenerCantidades(): void {
    this.cS.getCantidades().subscribe({
      next: (data) => {
        this.dataSource = data;
        this.mensaje = data.length === 0 ? 'No hay datos para mostrar.' : '';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'No se pudo cargar el reporte.';
        this.cdr.detectChanges();
      },
    });
  }
}
