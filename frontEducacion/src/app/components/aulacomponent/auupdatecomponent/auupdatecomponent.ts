import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AulaDTO } from '../../../models/AulaDTO';
import { Aulaservice } from '../../../services/aulaservice';
import { ColegioDTO } from '../../../models/ColegioDTO';
import { Colegioservice } from '../../../services/colegioservice';

@Component({
  selector: 'app-auupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatCheckboxModule],
  templateUrl: './auupdatecomponent.html',
  styleUrl: './auupdatecomponent.css',
})
export class Auupdatecomponent implements OnInit {
  id: number = 0;
  formAula: any;
  mensaje: string = '';
  listaColegio: ColegioDTO[] = [];

  constructor(
    private aS: Aulaservice,
    private cS: Colegioservice,
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /aula/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

    this.formAula = this.fb.group({
      idColegio: ['', Validators.required],
      grado: ['', Validators.required],
      seccion: ['', Validators.required],
      estado: [true],
    });

    this.cS.getColegio().subscribe({
      next: (data) => {
        this.listaColegio = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.aS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formAula.patchValue({
          idColegio: data.idColegio,
          grado: data.grado,
          seccion: data.seccion,
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

  actualizarAula(): void {
    if (this.formAula.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const aula: AulaDTO = {
      id: this.id,
      idColegio: this.formAula.value.idColegio,
      grado: this.formAula.value.grado,
      seccion: this.formAula.value.seccion,
      estado: this.formAula.value.estado,
      fechaCreacion: null, // lo escribe el backend
      actualizadoEn: null, // lo escribe el backend
    };

    this.aS.update(aula).subscribe({
      next: () => {
        this.mensaje = 'Aula actualizado correctamente.';
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
