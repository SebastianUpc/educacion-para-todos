import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { AulaInsertDTO } from '../../../models/AulaInsertDTO';
import { Aulaservice } from '../../../services/aulaservice';
import { ColegioDTO } from '../../../models/ColegioDTO';
import { Colegioservice } from '../../../services/colegioservice';

@Component({
  selector: 'app-auinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule, MatCheckboxModule],
  templateUrl: './auinsertcomponent.html',
  styleUrl: './auinsertcomponent.css',
})
export class Auinsertcomponent implements OnInit {
  formAula: any;
  mensaje: string = '';
  listaColegio: ColegioDTO[] = [];

  constructor(
    private aS: Aulaservice,
    private cS: Colegioservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
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
  }

  insertarAula(): void {
    if (this.formAula.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const aula: AulaInsertDTO = {
      id: 0,
      idColegio: this.formAula.value.idColegio,
      grado: this.formAula.value.grado,
      seccion: this.formAula.value.seccion,
      estado: this.formAula.value.estado,
    };

    this.aS.insertAula(aula).subscribe({
      next: () => {
        this.mensaje = 'Aula registrado correctamente.';
        this.formAula.reset({
          estado: true,
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
