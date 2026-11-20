import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
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
    private route: ActivatedRoute,
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
      next: () => {
        // El authGuard guarda en returnUrl la pantalla a la que se intentaba
        // entrar. Si existe, se vuelve ahi; si no, al inicio.
        const returnUrl = this.route.snapshot.queryParamMap.get('returnUrl');
        const destino =
          returnUrl && returnUrl.startsWith('/') && !returnUrl.startsWith('//')
            ? returnUrl
            : '/';

        this.router.navigateByUrl(destino);
      },
      error: (err) => {
        console.log(err);
        this.mensaje = 'Correo o contrasena incorrectos.';
        this.cdr.detectChanges();
      },
    });
  }
}
