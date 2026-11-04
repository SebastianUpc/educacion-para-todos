import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ColegioDTO } from '../../../models/ColegioDTO';
import { Colegioservice } from '../../../services/colegioservice';


@Component({
  selector: 'app-coupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './coupdatecomponent.html',
  styleUrl: './coupdatecomponent.css',
})
export class Coupdatecomponent implements OnInit {
  id: number = 0;
  formColegio: any;
  mensaje: string = '';

  constructor(
    private cS: Colegioservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /colegio/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

    this.formColegio = this.fb.group({
      nombre: ['', Validators.required],
      direccion: ['', Validators.required],
      distrito: ['', Validators.required],
    });

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.cS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formColegio.patchValue({
          nombre: data.nombre,
          direccion: data.direccion,
          distrito: data.distrito,
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

  actualizarColegio(): void {
    if (this.formColegio.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const colegio: ColegioDTO = {
      id: this.id,
      nombre: this.formColegio.value.nombre,
      direccion: this.formColegio.value.direccion,
      distrito: this.formColegio.value.distrito,
      fechaCreacion: null, // lo escribe el backend
      actualizadoEn: null, // lo escribe el backend
    };

    this.cS.update(colegio).subscribe({
      next: () => {
        this.mensaje = 'Colegio actualizado correctamente.';
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
