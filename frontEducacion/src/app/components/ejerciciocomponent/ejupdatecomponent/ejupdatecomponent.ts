import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { EjercicioInsertDTO } from '../../../models/EjercicioInsertDTO';
import { Ejercicioservice } from '../../../services/ejercicioservice';
import { TemaDTO } from '../../../models/TemaDTO';
import { Temaservice } from '../../../services/temaservice';

@Component({
  selector: 'app-ejupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatCheckboxModule],
  templateUrl: './ejupdatecomponent.html',
  styleUrl: './ejupdatecomponent.css',
})
export class Ejupdatecomponent implements OnInit {
  id: number = 0;
  formEjercicio: any;
  mensaje: string = '';
  listaTema: TemaDTO[] = [];

  opcionesEstado: any[] = [
    { value: 'ACTIVO', viewValue: 'ACTIVO' },
    { value: 'INACTIVO', viewValue: 'INACTIVO' },
  ];

  constructor(
    private eS: Ejercicioservice,
    private tS: Temaservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /ejercicio/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

    this.formEjercicio = this.fb.group({
      idTema: ['', Validators.required],
      enunciado: ['', Validators.required],
      contenidoJson: [''],
      respuestaCorrecta: ['', Validators.required],
      nivelDificultad: [1, [Validators.required, Validators.min(1)]],
      generadoPorIa: [false],
      estado: ['ACTIVO', [Validators.required, Validators.maxLength(20)]],
    });

    this.tS.getTema().subscribe({
      next: (data) => {
        this.listaTema = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.eS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formEjercicio.patchValue({
          idTema: data.idTema,
          enunciado: data.enunciado,
          contenidoJson: data.contenidoJson,
          respuestaCorrecta: data.respuestaCorrecta,
          nivelDificultad: data.nivelDificultad,
          generadoPorIa: data.generadoPorIa,
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

  actualizarEjercicio(): void {
    if (this.formEjercicio.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const ejercicio: EjercicioInsertDTO = {
      idTema: this.formEjercicio.value.idTema,
      enunciado: this.formEjercicio.value.enunciado,
      contenidoJson: this.formEjercicio.value.contenidoJson,
      respuestaCorrecta: this.formEjercicio.value.respuestaCorrecta,
      nivelDificultad: this.formEjercicio.value.nivelDificultad,
      generadoPorIa: this.formEjercicio.value.generadoPorIa,
      estado: this.formEjercicio.value.estado,
    };

    this.eS.update(this.id, ejercicio).subscribe({
      next: () => {
        this.mensaje = 'Ejercicio actualizado correctamente.';
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
