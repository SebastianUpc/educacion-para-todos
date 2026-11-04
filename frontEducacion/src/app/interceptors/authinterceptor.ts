import { HttpInterceptorFn } from '@angular/common/http';

// Agrega la cabecera Authorization: Bearer <token> a cada peticion.
// El backend protege todos los endpoints con @PreAuthorize, asi que sin
// esta cabecera la API responde 401 o 403.
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = sessionStorage.getItem('token');

  if (token && !req.url.endsWith('/login')) {
    const clonada = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
    return next(clonada);
  }

  return next(req);
};
