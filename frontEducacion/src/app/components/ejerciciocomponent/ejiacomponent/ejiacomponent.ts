import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { EstudianteResumenDTO } from '../../../models/EstudianteResumenDTO';
import { TemaDTO } from '../../../models/TemaDTO';
import { EjercicioInsertDTO } from '../../../models/EjercicioInsertDTO';
import { EjercicioIaRequestDTO } from '../../../models/EjercicioIaRequestDTO';
import { EjercicioIaResponseDTO } from '../../../models/EjercicioIaResponseDTO';
import { Ejercicioiaservice } from '../../../services/ejercicioiaservice';
import { Temaservice } from '../../../services/temaservice';

// Ejercicio propuesto por la IA, preparado para mostrarlo en pantalla
interface Propuesta {
  dto: EjercicioInsertDTO;
  alternativas: string[];
  explicacion: string;
  aprobado: boolean;
}

// HU-105: el docente elige estudiante y tema, revisa lo que propone la IA
// y guarda solo los ejercicios que aprueba.
@Component({
  selector: 'app-ejiacomponent',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './ejiacomponent.html',
  styleUrl: './ejiacomponent.css',
})
export class Ejiacomponent implements OnInit {
  formIa: any;
  listaEstudiantes: EstudianteResumenDTO[] = [];
  listaTema: TemaDTO[] = [];
  cantidades: number[] = [1, 2, 3, 4, 5];
  niveles: number[] = [1, 2, 3, 4, 5];

  generando: boolean = false;
  guardando: boolean = false;
  error: string = '';
  exito: string = '';

  resultado: EjercicioIaResponseDTO | null = null;
  propuestas: Propuesta[] = [];

  constructor(
    private iaS: Ejercicioiaservice,
    private tS: Temaservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formIa = this.fb.group({
      idEstudiante: ['', Validators.required],
      idTema: ['', Validators.required],
      cantidad: [3, [Validators.required, Validators.min(1), Validators.max(5)]],
      nivelDificultad: [null],
      necesidades: ['', Validators.maxLength(500)],
    });

    this.iaS.getEstudiantes().subscribe({
      next: (data) => {
        this.listaEstudiantes = data;

        // Viene del dashboard (HU-106) con ?estudiante=ID: se deja preseleccionado
        const preseleccion = Number(this.route.snapshot.queryParamMap.get('estudiante'));
        if (preseleccion && data.some((e) => e.id === preseleccion)) {
          this.formIa.patchValue({ idEstudiante: preseleccion });
        }

        if (data.length === 0) {
          this.error = 'No tiene estudiantes asignados en su aula.';
        }
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.error = err?.error?.message || 'No se pudo cargar la lista de estudiantes.';
        this.cdr.detectChanges();
      },
    });

    this.tS.getTema().subscribe({
      next: (data) => {
        this.listaTema = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
  }

  get cantidadAprobados(): number {
    return this.propuestas.filter((p) => p.aprobado).length;
  }

  generar(): void {
    if (this.formIa.invalid) {
      this.formIa.markAllAsTouched();
      return;
    }

    const dto: EjercicioIaRequestDTO = {
      idEstudiante: this.formIa.value.idEstudiante,
      idTema: this.formIa.value.idTema,
      cantidad: this.formIa.value.cantidad,
      nivelDificultad: this.formIa.value.nivelDificultad,
      necesidades: this.formIa.value.necesidades,
    };

    this.generando = true;
    this.error = '';
    this.exito = '';
    this.resultado = null;
    this.propuestas = [];
    this.cdr.detectChanges();

    this.iaS.generar(dto).subscribe({
      next: (data) => {
        this.resultado = data;
        this.propuestas = data.ejercicios.map((e) => this.aPropuesta(e));
        this.generando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        // CA08: se muestra el motivo y el formulario sigue disponible para reintentar
        this.error =
          err?.error?.message ||
          'No se pudo obtener ejercicios de la inteligencia artificial. Intente nuevamente.';
        this.generando = false;
        this.cdr.detectChanges();
      },
    });
  }

  guardar(): void {
    const aprobados = this.propuestas.filter((p) => p.aprobado).map((p) => p.dto);
    if (aprobados.length === 0) {
      this.error = 'Seleccione al menos un ejercicio para guardar.';
      return;
    }

    this.guardando = true;
    this.error = '';
    this.cdr.detectChanges();

    this.iaS.guardar(aprobados).subscribe({
      next: (data) => {
        this.exito = `Se guardaron ${data.length} ejercicio(s) generados por IA.`;
        this.resultado = null;
        this.propuestas = [];
        this.guardando = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.error = err?.error?.message || 'No se pudieron guardar los ejercicios.';
        this.guardando = false;
        this.cdr.detectChanges();
      },
    });
  }

  descartar(): void {
    this.resultado = null;
    this.propuestas = [];
    this.error = '';
  }

  private aPropuesta(dto: EjercicioInsertDTO): Propuesta {
    let alternativas: string[] = [];
    let explicacion = '';
    try {
      const contenido = JSON.parse(dto.contenidoJson || '{}');
      alternativas = Array.isArray(contenido.alternativas) ? contenido.alternativas : [];
      explicacion = typeof contenido.explicacion === 'string' ? contenido.explicacion : '';
    } catch {
      alternativas = [];
    }
    return { dto, alternativas, explicacion, aprobado: true };
  }
}
