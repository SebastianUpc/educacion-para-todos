import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { RolInsertDTO } from '../../../models/RolInsertDTO';
import { Rolservice } from '../../../services/rolservice';


@Component({
  selector: 'app-roinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './roinsertcomponent.html',
  styleUrl: './roinsertcomponent.css',
})
export class Roinsertcomponent implements OnInit {
  formRol: any;
  mensaje: string = '';

  constructor(
    private rS: Rolservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formRol = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
    });

  }

  insertarRol(): void {
    if (this.formRol.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const rol: RolInsertDTO = {
      id: 0,
      nombre: this.formRol.value.nombre,
      descripcion: this.formRol.value.descripcion,
    };

    this.rS.insertRol(rol).subscribe({
      next: () => {
        this.mensaje = 'Rol registrado correctamente.';
        this.formRol.reset();
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
