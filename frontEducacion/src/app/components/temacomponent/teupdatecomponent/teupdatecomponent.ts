import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TemaInsertDTO } from '../../../models/TemaInsertDTO';
import { Temaservice } from '../../../services/temaservice';


@Component({
  selector: 'app-teupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './teupdatecomponent.html',
  styleUrl: './teupdatecomponent.css',
})
export class Teupdatecomponent implements OnInit {
  id: number = 0;
  formTema: any;
  mensaje: string = '';

  constructor(
    private tS: Temaservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /tema/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

    this.formTema = this.fb.group({
      materia: ['', Validators.required],
      nombre: ['', Validators.required],
    });

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.tS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formTema.patchValue({
          materia: data.materia,
          nombre: data.nombre,
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

  actualizarTema(): void {
    if (this.formTema.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const tema: TemaInsertDTO = {
      materia: this.formTema.value.materia,
      nombre: this.formTema.value.nombre,
    };

    this.tS.update(this.id, tema).subscribe({
      next: () => {
        this.mensaje = 'Tema actualizado correctamente.';
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
