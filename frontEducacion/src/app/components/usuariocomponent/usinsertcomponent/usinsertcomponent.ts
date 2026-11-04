import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { UsuarioDTO } from '../../../models/UsuarioDTO';
import { Usuarioservice } from '../../../services/usuarioservice';
import { RolDTO } from '../../../models/RolDTO';
import { Rolservice } from '../../../services/rolservice';
import { AulaDTO } from '../../../models/AulaDTO';
import { Aulaservice } from '../../../services/aulaservice';

@Component({
  selector: 'app-usinsertcomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './usinsertcomponent.html',
  styleUrl: './usinsertcomponent.css',
})
export class Usinsertcomponent implements OnInit {
  formUsuario: any;
  mensaje: string = '';
  listaRol: RolDTO[] = [];
  listaAula: AulaDTO[] = [];
  listaUsuario: UsuarioDTO[] = [];

  constructor(
    private uS: Usuarioservice,
    private rS: Rolservice,
    private aS: Aulaservice,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.formUsuario = this.fb.group({
      nombre: ['', Validators.required],
      apellido: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      idRol: ['', Validators.required],
      idAula: [''],
      idApoderado: [''],
    });

    this.rS.getRol().subscribe({
      next: (data) => {
        this.listaRol = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
    this.aS.getAula().subscribe({
      next: (data) => {
        this.listaAula = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
    this.uS.getUsuario().subscribe({
      next: (data) => {
        this.listaUsuario = data;
        this.cdr.detectChanges();
      },
      error: (err) => console.log(err),
    });
  }

  insertarUsuario(): void {
    if (this.formUsuario.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const usuario: UsuarioDTO = {
      id: 0,
      nombre: this.formUsuario.value.nombre,
      apellido: this.formUsuario.value.apellido,
      email: this.formUsuario.value.email,
      password: this.formUsuario.value.password,
      idRol: this.formUsuario.value.idRol,
      idAula: this.formUsuario.value.idAula === '' ? null : this.formUsuario.value.idAula,
      idApoderado: this.formUsuario.value.idApoderado === '' ? null : this.formUsuario.value.idApoderado,
      estado: '', // el backend lo asigna
    };

    this.uS.insertUsuario(usuario).subscribe({
      next: () => {
        this.mensaje = 'Usuario registrado correctamente.';
        this.formUsuario.reset();
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
