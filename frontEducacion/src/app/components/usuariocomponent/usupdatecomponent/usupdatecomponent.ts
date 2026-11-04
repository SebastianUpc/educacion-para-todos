import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
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
  selector: 'app-usupdatecomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatSelectModule],
  templateUrl: './usupdatecomponent.html',
  styleUrl: './usupdatecomponent.css',
})
export class Usupdatecomponent implements OnInit {
  id: number = 0;
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
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // El id llega por la URL: /usuario/editar/:id
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.id || isNaN(this.id)) {
      this.mensaje = 'El id recibido no es valido.';
      return;
    }

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

    this.buscarPorId();
  }

  buscarPorId(): void {
    this.uS.listId(this.id).subscribe({
      next: (data: any) => {
        this.formUsuario.patchValue({
          nombre: data.nombre,
          apellido: data.apellido,
          email: data.email,
          password: data.password,
          idRol: data.idRol,
          idAula: data.idAula,
          idApoderado: data.idApoderado,
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

  actualizarUsuario(): void {
    if (this.formUsuario.invalid) {
      this.mensaje = 'Complete correctamente todos los campos obligatorios.';
      return;
    }

    const usuario: UsuarioDTO = {
      id: this.id,
      nombre: this.formUsuario.value.nombre,
      apellido: this.formUsuario.value.apellido,
      email: this.formUsuario.value.email,
      password: this.formUsuario.value.password,
      idRol: this.formUsuario.value.idRol,
      idAula: this.formUsuario.value.idAula === '' ? null : this.formUsuario.value.idAula,
      idApoderado: this.formUsuario.value.idApoderado === '' ? null : this.formUsuario.value.idApoderado,
      estado: '', // el backend lo asigna
    };

    this.uS.update(usuario).subscribe({
      next: () => {
        this.mensaje = 'Usuario actualizado correctamente.';
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
