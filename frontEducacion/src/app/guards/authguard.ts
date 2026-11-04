import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Loginservice } from '../services/loginservice';

// Impide entrar a las pantallas de datos sin haber iniciado sesion.
export const authGuard: CanActivateFn = () => {
  const lS = inject(Loginservice);
  const router = inject(Router);

  if (lS.estaLogueado()) {
    return true;
  }

  router.navigate(['/login']);
  return false;
};
