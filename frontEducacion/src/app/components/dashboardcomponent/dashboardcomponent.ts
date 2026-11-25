import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Menucomponent } from '../menucomponent/menucomponent';
import { DashboardDTO } from '../../models/DashboardDTO';
import { EstudianteRendimientoDTO } from '../../models/EstudianteRendimientoDTO';
import { RendimientoTemaDTO } from '../../models/RendimientoTemaDTO';
import { Dashboardservice } from '../../services/dashboardservice';

// HU-106: dashboard de seguimiento academico del docente.
@Component({
  selector: 'app-dashboardcomponent',
  imports: [
    Menucomponent,
    RouterLink,
    DecimalPipe,
    BaseChartDirective,
    MatCardModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatTableModule,
    MatPaginatorModule,
    MatTooltipModule,
  ],
  templateUrl: './dashboardcomponent.html',
  styleUrl: './dashboardcomponent.css',
})
export class Dashboardcomponent implements OnInit {
  dashboard: DashboardDTO | null = null;
  cargando: boolean = false;
  error: string = '';

  columnas: string[] = ['nombre', 'aula', 'promedio', 'aciertos', 'progreso', 'tema', 'motivos', 'accion'];
  estudiantes = new MatTableDataSource<EstudianteRendimientoDTO>();

  @ViewChild(MatPaginator) set paginator(paginator: MatPaginator) {
    if (paginator) {
      this.estudiantes.paginator = paginator;
    }
  }

  // Una sola serie: un solo color (el azul institucional) y sin leyenda;
  // el titulo de la tarjeta ya dice que se grafica.
  chartData: ChartData<'bar'> = { labels: [], datasets: [] };
  temasGrafico: RendimientoTemaDTO[] = [];

  chartOptions: ChartOptions<'bar'> = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: { display: false },
      tooltip: {
        callbacks: {
          label: (ctx) => {
            const tema = this.temasGrafico[ctx.dataIndex];
            if (!tema) {
              return `${ctx.parsed.y}%`;
            }
            return [
              `Aciertos: ${tema.porcentajeAciertos}% (${tema.respuestasCorrectas} de ${tema.totalRespuestas})`,
              `Promedio: ${tema.promedio} / 20`,
            ];
          },
        },
      },
    },
    scales: {
      x: {
        grid: { display: false },
        ticks: { color: '#3d4a57' },
      },
      y: {
        min: 0,
        max: 100,
        ticks: { stepSize: 25, color: '#5a6b7d', callback: (v) => `${v}%` },
        grid: { color: '#e6ebf1' },
        border: { display: false },
        title: { display: true, text: '% de respuestas correctas', color: '#5a6b7d' },
      },
    },
  };

  constructor(
    private dS: Dashboardservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.cargar();
  }

  get promedioAprobado(): boolean {
    const d = this.dashboard;
    return !!d && d.promedioAcademico !== null && d.promedioAcademico >= d.notaMinimaAprobatoria;
  }

  aprobado(nota: number | null): boolean {
    return nota !== null && this.dashboard !== null && nota >= this.dashboard.notaMinimaAprobatoria;
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';
    this.cdr.detectChanges();

    this.dS.getDashboard().subscribe({
      next: (data) => {
        this.dashboard = data;
        this.estudiantes.data = data.estudiantesConDificultades;
        this.armarGrafico(data.rendimientoPorTema);
        this.cargando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        // CA09: se informa el error y se conserva lo que ya se mostraba
        this.error =
          err?.error?.message ||
          'No se pudo obtener la información académica. Revise su conexión e intente nuevamente.';
        this.cargando = false;
        this.cdr.detectChanges();
      },
    });
  }

  private armarGrafico(temas: RendimientoTemaDTO[]): void {
    this.temasGrafico = temas;
    this.chartData = {
      labels: temas.map((t) => t.tema),
      datasets: [
        {
          label: '% de aciertos',
          data: temas.map((t) => t.porcentajeAciertos ?? 0),
          backgroundColor: '#2f6fb5',
          hoverBackgroundColor: '#1a4f8a',
          borderRadius: { topLeft: 4, topRight: 4 },
          borderSkipped: 'bottom',
          maxBarThickness: 48,
          categoryPercentage: 0.7,
        },
      ],
    };
  }
}
