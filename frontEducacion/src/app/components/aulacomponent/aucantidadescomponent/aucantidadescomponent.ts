import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { AulasPorColegioDTO } from '../../../models/AulasPorColegioDTO';
import { Aulaservice } from '../../../services/aulaservice';

@Component({
  selector: 'app-aucantidadescomponent',
  imports: [MatTableModule],
  templateUrl: './aucantidadescomponent.html',
  styleUrl: './aucantidadescomponent.css',
})
export class Aucantidadescomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2'];
  dataSource: AulasPorColegioDTO[] = [];
  mensaje: string = '';

  constructor(
    private aS: Aulaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerCantidades();
  }

  obtenerCantidades(): void {
    this.aS.getCantidades().subscribe({
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
