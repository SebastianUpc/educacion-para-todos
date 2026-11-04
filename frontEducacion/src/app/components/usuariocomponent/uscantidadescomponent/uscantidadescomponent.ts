import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { UsuariosPorRolDTO } from '../../../models/UsuariosPorRolDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-uscantidadescomponent',
  imports: [MatTableModule],
  templateUrl: './uscantidadescomponent.html',
  styleUrl: './uscantidadescomponent.css',
})
export class Uscantidadescomponent implements OnInit {
  displayedColumns: string[] = ['c1', 'c2'];
  dataSource: UsuariosPorRolDTO[] = [];
  mensaje: string = '';

  constructor(
    private uS: Usuarioservice,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.obtenerCantidades();
  }

  obtenerCantidades(): void {
    this.uS.getCantidades().subscribe({
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
