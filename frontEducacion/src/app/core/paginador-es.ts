import { Injectable } from '@angular/core';
import { MatPaginatorIntl } from '@angular/material/paginator';

// Textos del mat-paginator en espanol. Se registra una sola vez en app.config.ts
// y aplica a todas las tablas de la aplicacion.
@Injectable()
export class PaginadorEs extends MatPaginatorIntl {
  override itemsPerPageLabel = 'Registros por pagina:';
  override nextPageLabel = 'Pagina siguiente';
  override previousPageLabel = 'Pagina anterior';
  override firstPageLabel = 'Primera pagina';
  override lastPageLabel = 'Ultima pagina';

  override getRangeLabel = (page: number, pageSize: number, length: number): string => {
    if (length === 0 || pageSize === 0) {
      return `0 de ${length}`;
    }
    const inicio = page * pageSize;
    const fin = Math.min(inicio + pageSize, length);
    return `${inicio + 1} - ${fin} de ${length}`;
  };
}
