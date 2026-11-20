import { Routes } from '@angular/router';
import { Homecomponent } from './components/homecomponent/homecomponent';
import { Logincomponent } from './components/logincomponent/logincomponent';
import { authGuard } from './core/guards/auth-guard';
import { roleGuard } from './core/guards/role-guard';
import { Accesodenegadocomponent } from './components/accesodenegadocomponent/accesodenegadocomponent';
import { Colegiocomponent } from './components/colegiocomponent/colegiocomponent';
import { Colistcomponent } from './components/colegiocomponent/colistcomponent/colistcomponent';
import { Coinsertcomponent } from './components/colegiocomponent/coinsertcomponent/coinsertcomponent';
import { Coupdatecomponent } from './components/colegiocomponent/coupdatecomponent/coupdatecomponent';
import { Aulacomponent } from './components/aulacomponent/aulacomponent';
import { Aulistcomponent } from './components/aulacomponent/aulistcomponent/aulistcomponent';
import { Auinsertcomponent } from './components/aulacomponent/auinsertcomponent/auinsertcomponent';
import { Auupdatecomponent } from './components/aulacomponent/auupdatecomponent/auupdatecomponent';
import { Rolcomponent } from './components/rolcomponent/rolcomponent';
import { Rolistcomponent } from './components/rolcomponent/rolistcomponent/rolistcomponent';
import { Roinsertcomponent } from './components/rolcomponent/roinsertcomponent/roinsertcomponent';
import { Roupdatecomponent } from './components/rolcomponent/roupdatecomponent/roupdatecomponent';
import { Temacomponent } from './components/temacomponent/temacomponent';
import { Telistcomponent } from './components/temacomponent/telistcomponent/telistcomponent';
import { Teinsertcomponent } from './components/temacomponent/teinsertcomponent/teinsertcomponent';
import { Teupdatecomponent } from './components/temacomponent/teupdatecomponent/teupdatecomponent';
import { Ejerciciocomponent } from './components/ejerciciocomponent/ejerciciocomponent';
import { Ejlistcomponent } from './components/ejerciciocomponent/ejlistcomponent/ejlistcomponent';
import { Ejinsertcomponent } from './components/ejerciciocomponent/ejinsertcomponent/ejinsertcomponent';
import { Ejupdatecomponent } from './components/ejerciciocomponent/ejupdatecomponent/ejupdatecomponent';
import { Usuariocomponent } from './components/usuariocomponent/usuariocomponent';
import { Uslistcomponent } from './components/usuariocomponent/uslistcomponent/uslistcomponent';
import { Usinsertcomponent } from './components/usuariocomponent/usinsertcomponent/usinsertcomponent';
import { Usupdatecomponent } from './components/usuariocomponent/usupdatecomponent/usupdatecomponent';
import { Usbuscarcomponent } from './components/usuariocomponent/usbuscarcomponent/usbuscarcomponent';
import { Evaluacioncomponent } from './components/evaluacioncomponent/evaluacioncomponent';
import { Evlistcomponent } from './components/evaluacioncomponent/evlistcomponent/evlistcomponent';
import { Evinsertcomponent } from './components/evaluacioncomponent/evinsertcomponent/evinsertcomponent';
import { Evupdatecomponent } from './components/evaluacioncomponent/evupdatecomponent/evupdatecomponent';
import { Evbuscarcomponent } from './components/evaluacioncomponent/evbuscarcomponent/evbuscarcomponent';
import { Evhistorialcomponent } from './components/evaluacioncomponent/evhistorialcomponent/evhistorialcomponent';
import { Progresocomponent } from './components/progresocomponent/progresocomponent';
import { Prlistcomponent } from './components/progresocomponent/prlistcomponent/prlistcomponent';
import { Reportcomponent } from './components/reportcomponent/reportcomponent';
import { Report01component } from './components/reportcomponent/report01component/report01component';
import { Report02component } from './components/reportcomponent/report02component/report02component';
import { Report03component } from './components/reportcomponent/report03component/report03component';
import { Report04component } from './components/reportcomponent/report04component/report04component';
import { Detalleevaluacioncomponent } from './components/detalleevaluacioncomponent/detalleevaluacioncomponent';
import { Delistcomponent } from './components/detalleevaluacioncomponent/delistcomponent/delistcomponent';

export const routes: Routes = [
  { path: '', component: Homecomponent },
  { path: 'login', component: Logincomponent },
  {
    path: 'colegio',
    component: Colegiocomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Colistcomponent },
      { path: 'insertar', component: Coinsertcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
      { path: 'editar/:id', component: Coupdatecomponent, canActivate: [roleGuard], data: { roles: ['ADMIN'] } },
    ],
  },
  {
    path: 'aula',
    component: Aulacomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Aulistcomponent },
      { path: 'insertar', component: Auinsertcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
      { path: 'editar/:id', component: Auupdatecomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
    ],
  },
  {
    path: 'rol',
    component: Rolcomponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN'] },
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Rolistcomponent },
      { path: 'insertar', component: Roinsertcomponent },
      { path: 'editar/:id', component: Roupdatecomponent },
    ],
  },
  {
    path: 'tema',
    component: Temacomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Telistcomponent },
      { path: 'insertar', component: Teinsertcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
      { path: 'editar/:id', component: Teupdatecomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
    ],
  },
  {
    path: 'ejercicio',
    component: Ejerciciocomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Ejlistcomponent },
      { path: 'insertar', component: Ejinsertcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
      { path: 'editar/:id', component: Ejupdatecomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
    ],
  },
  {
    path: 'usuario',
    component: Usuariocomponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Uslistcomponent },
      { path: 'insertar', component: Usinsertcomponent },
      { path: 'editar/:id', component: Usupdatecomponent },
      { path: 'buscar', component: Usbuscarcomponent },
    ],
  },
  {
    path: 'evaluacion',
    component: Evaluacioncomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Evlistcomponent },
      { path: 'insertar', component: Evinsertcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
      { path: 'editar/:id', component: Evupdatecomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
      { path: 'buscar', component: Evbuscarcomponent },
      { path: 'historial', component: Evhistorialcomponent, canActivate: [roleGuard], data: { roles: ['ADMIN', 'DOCENTE'] } },
    ],
  },
  {
    path: 'progreso',
    component: Progresocomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Prlistcomponent },
    ],
  },
  {
    path: 'detalle-evaluacion',
    component: Detalleevaluacioncomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Delistcomponent },
    ],
  },
  {
    path: 'reportes',
    component: Reportcomponent,
    canActivate: [authGuard, roleGuard],
    data: { roles: ['ADMIN', 'DOCENTE'] },
    children: [
      { path: '', redirectTo: 'reporte01', pathMatch: 'full' },
      { path: 'reporte01', component: Report01component },
      { path: 'reporte02', component: Report02component },
      { path: 'reporte03', component: Report03component },
      { path: 'reporte04', component: Report04component },
    ],
  },
  { path: 'acceso-denegado', component: Accesodenegadocomponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' },
];
