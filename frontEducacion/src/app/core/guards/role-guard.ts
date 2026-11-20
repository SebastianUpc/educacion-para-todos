import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Loginservice } from '../../services/loginservice';

// Los roles permitidos se declaran en la ruta con data: { roles: ['ADMIN', ...] }.
// Asi la pantalla ni siquiera se carga si el perfil no corresponde, en vez de
// cargarla y que la API responda 403.
export const roleGuard: CanActivateFn = (route, state) => {
  const auth = inject(Loginservice);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    return router.createUrlTree(['/login'], {
      queryParams: { returnUrl: state.url },
    });
  }

  const allowedRoles = route.data['roles'];

  if (
    !Array.isArray(allowedRoles) ||
    !allowedRoles.every((role) => typeof role === 'string') ||
    !auth.hasAnyRole(allowedRoles)
  ) {
    return router.createUrlTree(['/acceso-denegado']);
  }

  return true;
};
