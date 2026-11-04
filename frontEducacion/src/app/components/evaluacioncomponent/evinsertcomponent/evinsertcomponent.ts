import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-evinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './evinsertcomponent.html',
  styleUrl: './evinsertcomponent.css',
})
export class Evinsertcomponent implements OnInit {
  formEvaluacion: any;
  mensaje: string = '';
  listaUsuario: UsuarioDTO[] = [];

  opcionesEstado: any[] = [
    { value: 'EN_CURSO', viewValue: 'EN_CURSO' },
    { value: 'FINALIZADA', viewValue: 'FINALIZADA' },
  ];

  constructor(
    private eS: Evaluacionservice,
    private uS: Usuarioservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formEvaluacion = this.fb.group({
      idEstudiante: ['', Validators.required],
      estado: ['EN_CURSO'],
    });

    this.uS.getUsuario().subscribe({
      next: (data) => {
        this.listaUsuario = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
  }

  insertarEvaluacion(): void {
    if (this.formEvaluacion.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const evaluacion: EvaluacionDTO = {
      id: 0,
      idEstudiante: this.formEvaluacion.value.idEstudiante,
      estado: this.formEvaluacion.value.estado,
      fechaInicio: null, // lo escribe el backend
      fechaFin: null, // lo escribe el backend
    };

    this.eS.insertEvaluacion(evaluacion).subscribe({
      next: () => {
        this.mensaje = 'Evaluacion registrado correctamente.';
        this.formEvaluacion.reset({
          estado: 'EN_CURSO',
        });
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = err?.error?.message || 'Error al registrar el registro.';
        this.cdr.detectChanges();
      },
    });
  }
}
