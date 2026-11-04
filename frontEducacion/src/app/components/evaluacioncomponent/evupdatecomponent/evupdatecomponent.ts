import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { EvaluacionDTO } from '../../../models/EvaluacionDTO';
import { Evaluacionservice } from '../../../services/evaluacionservice';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';

@Component({
  selector: 'app-evupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './evupdatecomponent.html',
  styleUrl: './evupdatecomponent.css',
})
export class Evupdatecomponent implements OnInit {
  id: number = 0;
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
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /evaluacion/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

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

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.eS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formEvaluacion.patchValue({
          idEstudiante: data.idEstudiante,
          estado: data.estado,
        });
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'No se encontro el registro con el id ' + this.id;
        this.cdr.detectChanges();
      },
    });
  }

  actualizarEvaluacion(): void {
    if (this.formEvaluacion.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const evaluacion: EvaluacionDTO = {
      id: this.id,
      idEstudiante: this.formEvaluacion.value.idEstudiante,
      estado: this.formEvaluacion.value.estado,
      fechaInicio: null, // lo escribe el backend
      fechaFin: null, // lo escribe el backend
    };

    this.eS.update(evaluacion).subscribe({
      next: () => {
        this.mensaje = 'Evaluacion actualizado correctamente.';
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.log(err);
        this.mensaje = err?.error?.message || 'Error al actualizar el registro.';
        this.cdr.detectChanges();
      },
    });
  }
}
