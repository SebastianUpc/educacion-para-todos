import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { LoginRequest } from '../../models/LoginRequest';
import { Loginservice } from '../../services/loginservice';

@Component({
  selector: 'app-logincomponent',
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatCardModule],
  templateUrl: './logincomponent.html',
  styleUrl: './logincomponent.css',
})
export class Logincomponent {
  formLogin: any;
  mensaje: string = '';

  constructor(
    private lS: Loginservice,
    private fb: FormBuilder,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {
    this.formLogin = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  ingresar(): void {
    if (this.formLogin.invalid) {
      this.mensaje = 'Ingrese su correo y su contrasena.';
      return;
    }

    const credenciales: LoginRequest = {
      username: this.formLogin.value.username,
      password: this.formLogin.value.password,
    };

    this.lS.login(credenciales).subscribe({
      next: (data) => {
        this.lS.guardarSesion(data);
        this.router.navigate(['/usuario/listar']);
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'Correo o contrasena incorrectos.';
        this.cdr.detectChanges();
      },
    });
  }
}
