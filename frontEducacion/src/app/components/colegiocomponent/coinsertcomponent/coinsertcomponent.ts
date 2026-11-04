import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ColegioInsertDTO } from '../../../models/ColegioInsertDTO';
import { Colegioservice } from '../../../services/colegioservice';


@Component({
  selector: 'app-coinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './coinsertcomponent.html',
  styleUrl: './coinsertcomponent.css',
})
export class Coinsertcomponent implements OnInit {
  formColegio: any;
  mensaje: string = '';

  constructor(
    private cS: Colegioservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formColegio = this.fb.group({
      nombre: ['', Validators.required],
      direccion: ['', Validators.required],
      distrito: ['', Validators.required],
    });

  }

  insertarColegio(): void {
    if (this.formColegio.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const colegio: ColegioInsertDTO = {
      id: 0,
      nombre: this.formColegio.value.nombre,
      direccion: this.formColegio.value.direccion,
      distrito: this.formColegio.value.distrito,
    };

    this.cS.insertColegio(colegio).subscribe({
      next: () => {
        this.mensaje = 'Colegio registrado correctamente.';
        this.formColegio.reset();
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
