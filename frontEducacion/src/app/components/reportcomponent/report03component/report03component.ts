import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ChartData, ChartOptions } from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { UsuariosPorRolDTO } from '../../../models/UsuariosPorRolDTO';
import { Usuarioservice } from '../../../services/usuarioservice';
type TipoGrafico = 'bar' | 'line' | 'pie' | 'doughnut' | 'radar';

@Component({
  selector: 'app-report03component',
  imports: [BaseChartDirective],
  templateUrl: './report03component.html',
  styleUrl: './report03component.css',
})
export class Report03component implements OnInit {
  lista: UsuariosPorRolDTO[] = [];

  public chartType: TipoGrafico = 'bar';

  public chartData: ChartData<TipoGrafico> = {
    labels: [],
    datasets: [
      {
        label: 'Cantidad de usuarios',
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
    private uS: Usuarioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtener();
  }

  obtener(): void {
    this.uS.getCantidades().subscribe({
      next: (data: UsuariosPorRolDTO[]) => {
        this.lista = data;

        this.chartData = {
          labels: data.map((item) => item.nombreRol),
          datasets: [
            {
              label: 'Cantidad de usuarios',
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
