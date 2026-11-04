# frontEducacion — Frontend Angular de "Educación para Todos"

Angular 21 + Angular Material. Consume la API de Spring Boot del proyecto
`educacionparatodos`. Sigue el mismo patrón que el `demoFront8254` de la
profesora (semanas 10 y 11): componentes standalone, `ChangeDetectorRef`,
bloques `@if` / `@for`, un service por entidad y rutas hijas.

---

## 1. Cómo levantarlo

### Paso 1 — Instalar dependencias

Abre la carpeta en VS Code y en la terminal:

```
npm install
```

### Paso 2 — Levantar el backend

El frontend apunta a `http://localhost:8080` (ver `src/environments/environment.ts`).
Arranca `educacionparatodos` en IntelliJ **antes** de usar el frontend.

### Paso 3 — Levantar Angular

```
ng serve
```

Abre `http://localhost:4200`.

### Paso 4 — Iniciar sesión

La API protege **todos** los endpoints con `@PreAuthorize`, así que lo primero
es entrar en `/login`. Usa la cuenta de administrador del proyecto.

---

## 2. Qué se cambió en el backend

En `SecurityConfig.java` se agregó el bean `corsConfigurationSource()`.

**Por qué:** ya existía la línea `.cors(Customizer.withDefaults())`, pero sin el
bean no había ninguna configuración que aplicar, así que el navegador bloqueaba
toda petición que viniera de `http://localhost:4200`. Es el mismo patrón que la
profesora usa en `demoVI` (`S10-CORS.pdf`, opción 2).

Si en algún momento despliegas el frontend, hay que agregar su dominio a
`setAllowedOrigins`.

---

## 3. Estructura

```
src/app/
├── app.config.ts            provideHttpClient + withInterceptors
├── app.routes.ts            rutas hijas por entidad, protegidas con authGuard
├── guards/
│   └── authguard.ts         bloquea las pantallas si no hay token
├── interceptors/
│   └── authinterceptor.ts   agrega Authorization: Bearer <token>
├── models/                  un DTO de TypeScript por DTO de Java
├── services/                un service por entidad + loginservice
└── components/
    ├── logincomponent/
    ├── homecomponent/
    ├── menucomponent/       mat-toolbar + mat-menu
    └── <entidad>component/
        ├── <entidad>component.*        contenedor: menú + router-outlet
        ├── xxlistcomponent/            listar + botones editar/eliminar
        ├── xxinsertcomponent/          formulario reactivo de registro
        ├── xxupdatecomponent/          formulario de edición (lee :id de la URL)
        ├── xxbuscarcomponent/          búsqueda por estado (@RequestParam)
        └── xxcantidadescomponent/      reporte de la query con JOIN
```

---

## 3.1. Convención de nombres (la misma del `demoFront8254`)

Tomada tal cual del `cropservice.ts` y de los componentes de la profesora.

**En el service** — el nombre de la entidad va pegado al verbo, salvo en
`listId` y `update`, que ella deja sin sufijo:

| Profesora (`cropservice`) | Tu proyecto (`usuarioservice`) |
|---|---|
| `getCrop()` | `getUsuario()` |
| `insertCrop(crop)` | `insertUsuario(usuario)` |
| `deleteCrop(id)` | `deleteUsuario(id)` |
| `listId(id)` | `listId(id)` |
| `update(dto)` | `update(dto)` |
| `getVariety(vari)` | `getEstado(estado)` |
| — | `getCantidades()` |

**En los componentes:**

| Profesora | Tu proyecto |
|---|---|
| `obtenerCultivos()` | `obtenerUsuarios()` |
| `eliminarCultivo(id)` | `eliminarUsuario(id)` |
| `insertarCultivo()` | `insertarUsuario()` |
| `actualizarCultivo()` | `actualizarUsuario()` |
| `buscarPorVariedad()` | `buscarPorEstado()` |
| `cultivoForm` / `formCultivo` | `formUsuario` |
| `private cS: Cropservice` | `private uS: Usuarioservice` |

**El objeto que se envía va tipado**, no con `any`, igual que ella hace
`const cultivo: CropDTOInsert = {...}`:

```ts
const usuario: UsuarioDTO = {
  id: 0,
  nombre: this.formUsuario.value.nombre,
  ...
  estado: '', // el backend lo asigna
};
```

Tiparlo obliga a mandar **todos** los campos del DTO. Por eso en los modelos
hay dos tipos que no son simples:

- `fechaCreacion`, `actualizadoEn`, `fechaInicio`, `fechaFin` → `string | null`.
  Los escribe el backend, el frontend manda `null`. Si mandara `''`, Jackson
  fallaría al convertirlo a `LocalDateTime`.
- `idAula`, `idApoderado` → `number | null`. Son opcionales; un `mat-select`
  sin elegir devuelve `''` y Jackson no sabe convertir `''` a `Long`, así que
  el componente lo pasa a `null` antes de enviarlo.

En el demo de la profesora no aparece ningún `| null` porque todos los campos
de `Crop` son obligatorios y las fechas las escribe el usuario con el
`matDatepicker`. En tu modelo no es el caso.

---

## 4. Entidades y lo que tiene cada una

| Entidad | Listar | Insertar | Editar | Eliminar | Buscar | Reporte JOIN |
|---|---|---|---|---|---|---|
| Colegio | sí | sí | sí | sí | — | Colegios por distrito |
| Aula | sí | sí | sí | sí | — | Aulas por colegio |
| Rol | sí | sí | sí | sí | — | — |
| Tema | sí | sí | sí | sí | — | — |
| Ejercicio | sí | sí | sí | sí | — | — |
| **Usuario** | sí | sí | sí | sí | por estado | **Usuarios por rol** |
| **Evaluación** | sí | sí | sí | sí | por estado | **Evaluaciones por estudiante** |
| Progreso | sí | — | — | — | — | — |
| Detalle de evaluación | sí | — | — | — | — | — |

Progreso y Detalle de evaluación solo tienen `GET` en el backend, por eso en el
frontend son solo de lectura.

Las dos filas en negrita son **tu parte del proyecto**: las consultas
`contarUsuariosPorRol()` (nativa con LEFT JOIN) y
`contarEvaluacionesPorEstudiante()` ya tienen su pantalla propia en
`/usuario/cantidades` y `/evaluacion/cantidades`.

---

## 5. Cómo funciona la autenticación

1. `logincomponent` hace `POST /login` con `{ username, password }`.
2. `loginservice.guardarSesion()` guarda el token en `sessionStorage`.
3. `authinterceptor` intercepta **cada** petición HTTP y le agrega la cabecera
   `Authorization: Bearer <token>`. Sin esto la API responde 401.
4. `authGuard` impide entrar a las pantallas de datos si no hay token.
5. `loginservice.getRoles()` lee el claim `roles` del JWT (el backend lo escribe
   en `JwtTokenService` como texto separado por comas, por ejemplo `ROLE_ADMIN`).

El token se guarda en `sessionStorage`, así que se borra al cerrar la pestaña.

---

## 6. Detalles del backend que condicionan el frontend

Estos no son errores del frontend, son cosas que la API exige:

**`UsuarioDTO.password` tiene `@NotBlank`.** El `PUT /api/usuarios` lleva
`@Valid`, así que la validación corre también al actualizar, aunque el
controller nunca toque la contraseña. Por eso el formulario de edición de
usuario mantiene el campo contraseña como obligatorio y muestra un aviso.

**`GET /api/ejercicios/{id}` no devuelve `respuestaCorrecta`.** `EjercicioDTO`
no tiene ese campo, pero `EjercicioInsertDTO` sí y es obligatorio. Al editar un
ejercicio hay que volver a escribir la respuesta correcta.

**`PUT` de Tema y Ejercicio llevan el id en la URL** (`/api/temas/{id}`),
mientras que Colegio, Aula, Rol, Usuario y Evaluación lo llevan en el cuerpo.
Los services respetan esa diferencia.

**`DELETE /api/usuarios/{id}` es un borrado lógico:** pone el estado en
`INACTIVO`. El usuario desaparece de `GET /api/usuarios` pero sigue en la base,
y el reporte de usuarios por rol lo sigue contando. Es un buen detalle para
sustentar.

---

## 7. Si algo falla

| Síntoma | Causa |
|---|---|
| Error de CORS en la consola | El backend no está levantado, o se perdió el bean `corsConfigurationSource` |
| 401 en todas las peticiones | No hay token: vuelve a iniciar sesión |
| 403 en una pantalla concreta | Tu rol no tiene permiso; mira el `@PreAuthorize` de ese endpoint |
| 400 al registrar | Falta un campo obligatorio; el mensaje del backend dice cuál |
| 409 al registrar un usuario | El correo ya existe |
| La tabla sale vacía sin error | La lista está vacía de verdad, o el filtro no coincide |
| `ng serve` no reconoce el comando | Usa `npx ng serve` o instala el CLI con `npm i -g @angular/cli` |

---

## 8. Pendiente

- Mostrar el nombre del colegio y del rol en las tablas en lugar del id
  (requiere un endpoint que los devuelva, o resolverlo en el frontend).
- Ocultar los botones de insertar, editar y eliminar según el rol, usando
  `loginservice.tieneRol()`.
- Paginación con `MatPaginator` cuando las tablas crezcan.
