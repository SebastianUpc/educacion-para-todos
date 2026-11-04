import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { RolDTO } from '../../../models/RolDTO';
import { Rolservice } from '../../../services/rolservice';


@Component({
  selector: 'app-roupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './roupdatecomponent.html',
  styleUrl: './roupdatecomponent.css',
})
export class Roupdatecomponent implements OnInit {
  id: number = 0;
  formRol: any;
  mensaje: string = '';

  constructor(
    private rS: Rolservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /rol/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

    this.formRol = this.fb.group({
      nombre: ['', Validators.required],
      descripcion: [''],
    });

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.rS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formRol.patchValue({
          nombre: data.nombre,
          descripcion: data.descripcion,
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

  actualizarRol(): void {
    if (this.formRol.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const rol: RolDTO = {
      id: this.id,
      nombre: this.formRol.value.nombre,
      descripcion: this.formRol.value.descripcion,
      fechaCreacion: null, // lo escribe el backend
      actualizadoEn: null, // lo escribe el backend
    };

    this.rS.update(rol).subscribe({
      next: () => {
        this.mensaje = 'Rol actualizado correctamente.';
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
