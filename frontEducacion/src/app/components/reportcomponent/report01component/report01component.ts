import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { ColegiosPorDistritoDTO } from '../../../models/ColegiosPorDistritoDTO';
import { Colegioservice } from '../../../services/colegioservice';
type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-report01component',
  imports: [BaseChartDirective],
  templateUrl: './report01component.html',
  styleUrl: './report01component.css',
})
export class Report01component implements OnInit {
  lista: ColegiosPorDistritoDTO[] = [];

  public chartType: TipoGrafico = 'pie';

  public chartData: ChartData<TipoGrafico> = {
    labels: [],
    datasets: [
      {
        label: 'Cantidad de colegios',
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
        position: 'right',
      },
    },
  };

  constructor(
    private cS: Colegioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtener();
  }

  obtener(): void {
    this.cS.getCantidades().subscribe({
      next: (data: ColegiosPorDistritoDTO[]) => {
        this.lista = data;

        this.chartData = {
          labels: data.map((item) => item.distrito),
          datasets: [
            {
              label: 'Cantidad de colegios',
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
