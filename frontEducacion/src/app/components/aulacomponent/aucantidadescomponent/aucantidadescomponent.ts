import { ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import Chart from 'chart.js/auto';
import { AulasPorColegioDTO } from '../../../models/AulasPorColegioDTO';
import { Aulaservice } from '../../../services/aulaservice';

@Component({
  selector: 'app-aucantidadescomponent',
  imports: [MatTableModule],
  templateUrl: './aucantidadescomponent.html',
  styleUrl: './aucantidadescomponent.css',
})
export class Aucantidadescomponent implements OnInit, OnDestroy {
  displayedColumns: string[] = ['c1', 'c2'];
  dataSource: AulasPorColegioDTO[] = [];
  mensaje: string = '';

  // Lienzo del template donde Chart.js dibuja el reporte
  @ViewChild('grafico') grafico!: ElementRef<HTMLCanvasElement>;
  private chart: Chart | null = null;

  // Paleta del reporte, en el azul del resto de la aplicacion
  private colores: string[] = [
    '#1a4f8a', '#2b7cc4', '#4da3e0', '#7bc0ec', '#a8d7f5',
    '#f9a825', '#ef6c00', '#c62828', '#6a1b9a', '#2e7d32',
  ];

  constructor(
    private aS: Aulaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerCantidades();
  }

  ngOnDestroy(): void {
    // Liberar el grafico al salir de la pantalla
    if (this.chart) {
      this.chart.destroy();
      this.chart = null;
    }
  }

  obtenerCantidades(): void {
    this.aS.getCantidades().subscribe({
      next: (data) => {
        this.dataSource = data;
        this.mensaje = data.length === 0 ? 'No hay datos para mostrar.' : '';
        this.cdr.detectChanges();
        this.dibujarGrafico();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'No se pudo cargar el reporte.';
        this.cdr.detectChanges();
      },
    });
  }

  dibujarGrafico(): void {
    if (!this.grafico || this.dataSource.length === 0) {
      return;
    }

    // Si ya habia un grafico dibujado hay que destruirlo antes de volver a dibujar
    if (this.chart) {
      this.chart.destroy();
    }

    this.chart = new Chart(this.grafico.nativeElement, {
      type: 'bar',
      data: {
        labels: this.dataSource.map((x) => x.nombreColegio),
        datasets: [
          {
            label: 'Cantidad',
            data: this.dataSource.map((x) => x.cantidad),
            backgroundColor: this.colores,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          title: { display: true, text: 'Aulas por colegio' },
        },
        scales: {
          y: { beginAtZero: true, ticks: { stepSize: 1 } },
        },
      },
    });
  }
}
