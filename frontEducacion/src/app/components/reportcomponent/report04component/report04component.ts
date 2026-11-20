import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { EvaluacionesPorEstudianteDTO } from '../../../models/EvaluacionesPorEstudianteDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';
type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-report04component',
  imports: [BaseChartDirective],
  templateUrl: './report04component.html',
  styleUrl: './report04component.css',
})
export class Report04component implements OnInit {
  lista: EvaluacionesPorEstudianteDTO[] = [];

  public chartType: TipoGrafico = 'bar';

  public chartData: ChartData<TipoGrafico> = {
    labels: [],
    datasets: [
      {
        label: 'Evaluaciones rendidas',
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
    private eS: Evaluacionservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtener();
  }

  obtener(): void {
    this.eS.getCantidades().subscribe({
      next: (data: EvaluacionesPorEstudianteDTO[]) => {
        this.lista = data;

        this.chartData = {
          labels: data.map((item) => item.nombre),
          datasets: [
            {
              label: 'Evaluaciones rendidas',
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
