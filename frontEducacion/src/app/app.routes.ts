import { Routes } from '@angular/router';
import { Homecomponent } from './components/homecomponent/homecomponent';
import { Logincomponent } from './components/logincomponent/logincomponent';
import { authGuard } from './guards/authguard';
import { Colegiocomponent } from './components/colegiocomponent/colegiocomponent';
import { Colistcomponent } from './components/colegiocomponent/colistcomponent/colistcomponent';
import { Coinsertcomponent } from './components/colegiocomponent/coinsertcomponent/coinsertcomponent';
import { Coupdatecomponent } from './components/colegiocomponent/coupdatecomponent/coupdatecomponent';
import { Cocantidadescomponent } from './components/colegiocomponent/cocantidadescomponent/cocantidadescomponent';
import { Aulacomponent } from './components/aulacomponent/aulacomponent';
import { Aulistcomponent } from './components/aulacomponent/aulistcomponent/aulistcomponent';
import { Auinsertcomponent } from './components/aulacomponent/auinsertcomponent/auinsertcomponent';
import { Auupdatecomponent } from './components/aulacomponent/auupdatecomponent/auupdatecomponent';
import { Aucantidadescomponent } from './components/aulacomponent/aucantidadescomponent/aucantidadescomponent';
import { Rolcomponent } from './components/rolcomponent/rolcomponent';
import { Rolistcomponent } from './components/rolcomponent/rolistcomponent/rolistcomponent';
import { Roinsertcomponent } from './components/rolcomponent/roinsertcomponent/roinsertcomponent';
import { Roupdatecomponent } from './components/rolcomponent/roupdatecomponent/roupdatecomponent';
// import { Temacomponent } from './components/temacomponent/temacomponent';
// import { Telistcomponent } from './components/temacomponent/telistcomponent/telistcomponent';
// import { Teinsertcomponent } from './components/temacomponent/teinsertcomponent/teinsertcomponent';
// import { Teupdatecomponent } from './components/temacomponent/teupdatecomponent/teupdatecomponent';
// import { Ejerciciocomponent } from './components/ejerciciocomponent/ejerciciocomponent';
// import { Ejlistcomponent } from './components/ejerciciocomponent/ejlistcomponent/ejlistcomponent';
// import { Ejinsertcomponent } from './components/ejerciciocomponent/ejinsertcomponent/ejinsertcomponent';
// import { Ejupdatecomponent } from './components/ejerciciocomponent/ejupdatecomponent/ejupdatecomponent';
import { Usuariocomponent } from './components/usuariocomponent/usuariocomponent';
import { Uslistcomponent } from './components/usuariocomponent/uslistcomponent/uslistcomponent';
import { Usinsertcomponent } from './components/usuariocomponent/usinsertcomponent/usinsertcomponent';
import { Usupdatecomponent } from './components/usuariocomponent/usupdatecomponent/usupdatecomponent';
import { Usbuscarcomponent } from './components/usuariocomponent/usbuscarcomponent/usbuscarcomponent';
import { Uscantidadescomponent } from './components/usuariocomponent/uscantidadescomponent/uscantidadescomponent';
import { Evaluacioncomponent } from './components/evaluacioncomponent/evaluacioncomponent';
import { Evlistcomponent } from './components/evaluacioncomponent/evlistcomponent/evlistcomponent';
import { Evinsertcomponent } from './components/evaluacioncomponent/evinsertcomponent/evinsertcomponent';
import { Evupdatecomponent } from './components/evaluacioncomponent/evupdatecomponent/evupdatecomponent';
import { Evbuscarcomponent } from './components/evaluacioncomponent/evbuscarcomponent/evbuscarcomponent';
import { Evcantidadescomponent } from './components/evaluacioncomponent/evcantidadescomponent/evcantidadescomponent';
import { Progresocomponent } from './components/progresocomponent/progresocomponent';
import { Prlistcomponent } from './components/progresocomponent/prlistcomponent/prlistcomponent';
import { Detalleevaluacioncomponent } from './components/detalleevaluacioncomponent/detalleevaluacioncomponent';
import { Delistcomponent } from './components/detalleevaluacioncomponent/delistcomponent/delistcomponent';

// ---------------------------------------------------------------------------
// PENDIENTE (Recoba): Tema y Ejercicio
// Los imports y los dos bloques de ruta de abajo estan comentados porque esos
// componentes todavia no estan en el repositorio. Al agregar las carpetas
// temacomponent/ y ejerciciocomponent/, descomentar:
//   1. los 8 imports marcados con //
//   2. los dos bloques de ruta  path: 'tema'  y  path: 'ejercicio'
// ---------------------------------------------------------------------------

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
      { path: 'insertar', component: Coinsertcomponent },
      { path: 'editar/:id', component: Coupdatecomponent },
      { path: 'cantidades', component: Cocantidadescomponent },
    ],
  },
  {
    path: 'aula',
    component: Aulacomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Aulistcomponent },
      { path: 'insertar', component: Auinsertcomponent },
      { path: 'editar/:id', component: Auupdatecomponent },
      { path: 'cantidades', component: Aucantidadescomponent },
    ],
  },
  {
    path: 'rol',
    component: Rolcomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Rolistcomponent },
      { path: 'insertar', component: Roinsertcomponent },
      { path: 'editar/:id', component: Roupdatecomponent },
    ],
  },
  // {
  // path: 'tema',
  // component: Temacomponent,
  // canActivate: [authGuard],
  // children: [
  // { path: '', redirectTo: 'listar', pathMatch: 'full' },
  // { path: 'listar', component: Telistcomponent },
  // { path: 'insertar', component: Teinsertcomponent },
  // { path: 'editar/:id', component: Teupdatecomponent },
  // ],
  // },
  // {
  // path: 'ejercicio',
  // component: Ejerciciocomponent,
  // canActivate: [authGuard],
  // children: [
  // { path: '', redirectTo: 'listar', pathMatch: 'full' },
  // { path: 'listar', component: Ejlistcomponent },
  // { path: 'insertar', component: Ejinsertcomponent },
  // { path: 'editar/:id', component: Ejupdatecomponent },
  // ],
  // },
  {
    path: 'usuario',
    component: Usuariocomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Uslistcomponent },
      { path: 'insertar', component: Usinsertcomponent },
      { path: 'editar/:id', component: Usupdatecomponent },
      { path: 'buscar', component: Usbuscarcomponent },
      { path: 'cantidades', component: Uscantidadescomponent },
    ],
  },
  {
    path: 'evaluacion',
    component: Evaluacioncomponent,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'listar', pathMatch: 'full' },
      { path: 'listar', component: Evlistcomponent },
      { path: 'insertar', component: Evinsertcomponent },
      { path: 'editar/:id', component: Evupdatecomponent },
      { path: 'buscar', component: Evbuscarcomponent },
      { path: 'cantidades', component: Evcantidadescomponent },
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
  { path: '**', redirectTo: '' },
];
