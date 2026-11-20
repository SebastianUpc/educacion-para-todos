import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { AulasPorColegioDTO } from '../../../models/AulasPorColegioDTO';
import { Aulaservice } from '../../../services/aulaservice';
type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-report02component',
  imports: [BaseChartDirective],
  templateUrl: './report02component.html',
  styleUrl: './report02component.css',
})
export class Report02component implements OnInit {
  lista: AulasPorColegioDTO[] = [];

  public chartType: TipoGrafico = 'bar';

  public chartData: ChartData<TipoGrafico> = {
    labels: [],
    datasets: [
      {
        label: 'Cantidad de aulas',
        data: [],
        borderWidth: 1,
      },
    ],
  };

  public chartOptions: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        display: true,
        position: 'bottom',
      },
    },
  };

  constructor(
    private aS: Aulaservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtener();
  }

  obtener(): void {
    this.aS.getCantidades().subscribe({
      next: (data: AulasPorColegioDTO[]) => {
        this.lista = data;

        this.chartData = {
          labels: data.map((item) => item.nombreColegio),
          datasets: [
            {
              label: 'Cantidad de aulas',
              data: data.map((item) => item.cantidad),
              borderWidth: 1,
            },
          ],
        };
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.log(error);
        this.cdr.detectChanges();
      },
    });
  }
}
