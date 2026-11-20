import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
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
  selector: 'app-ejinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatCheckboxModule],
  templateUrl: './ejinsertcomponent.html',
  styleUrl: './ejinsertcomponent.css',
})
export class Ejinsertcomponent implements OnInit {
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
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
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
  }

  insertarEjercicio(): void {
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

    this.eS.insertEjercicio(ejercicio).subscribe({
      next: () => {
        this.mensaje = 'Ejercicio registrado correctamente.';
        this.formEjercicio.reset({
          generadoPorIa: false,
          estado: 'ACTIVO',
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
