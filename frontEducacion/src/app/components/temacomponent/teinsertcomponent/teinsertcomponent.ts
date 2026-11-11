import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TemaInsertDTO } from '../../../models/TemaInsertDTO';
import { Temaservice } from '../../../services/temaservice';


@Component({
  selector: 'app-teinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './teinsertcomponent.html',
  styleUrl: './teinsertcomponent.css',
})
export class Teinsertcomponent implements OnInit {
  formTema: any;
  mensaje: string = '';

  constructor(
    private tS: Temaservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formTema = this.fb.group({
      materia: ['', Validators.required],
      nombre: ['', Validators.required],
    });

  }

  insertarTema(): void {
    if (this.formTema.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const tema: TemaInsertDTO = {
      materia: this.formTema.value.materia,
      nombre: this.formTema.value.nombre,
    };

    this.tS.insertTema(tema).subscribe({
      next: () => {
        this.mensaje = 'Tema registrado correctamente.';
        this.formTema.reset();
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
