# Resumen para IA Generativa de Frontend

## Instruccion directa para la IA que genere el frontend

Lee este archivo completo y úsalo como contrato de trabajo.

Debes generar un frontend de prueba para este backend Spring Boot con estas reglas:

- no inventar endpoints
- no inventar campos fuera de los DTOs documentados aquí
- usar `codigo` como identificador funcional principal
- respetar la paginación del backend
- respetar las bajas lógicas y reactivaciones
- mostrar errores del backend con prioridad sobre validaciones inventadas del frontend
- construir una interfaz administrativa tipo ERP
- usar navegación lateral
- implementar primero módulos maestros reutilizables y luego `productoCompleto`

Objetivo de esta primera versión:

- una interfaz gráfica para probar el backend
- alta, listado, consulta, modificación, desactivación y reactivación donde corresponda
- sin login
- con selector global de sucursal para pantallas de producto

No debes reinterpretar decisiones ya cerradas en este documento. Si algo no está documentado, debes preferir una solución simple, consistente y reutilizable, sin agregar lógica de negocio nueva.

Este documento resume el backend actual para que una IA generativa pueda construir un frontend de prueba funcional sin inventar contratos ni lógica de negocio.

Importante:

- los modulos maestros (`categoria`, `proveedor`, `impuesto`, `listaPrecio`, `sucursal`) siguen vigentes como se describen aqui
- el contrato de `productoCompleto` fue refactorizado despues de la primera version de este documento
- para costos y precios de `productoCompleto`, la referencia correcta ahora es [PRECIOS_Y_COSTOS_DISENO.md](/F:/programacion/Serios/sistema-stock/backend/docs/PRECIOS_Y_COSTOS_DISENO.md)
- esta seccion ya fue parcialmente resincronizada, pero ante dudas debe prevalecer el documento de costos y precios

## Objetivo del frontend

El frontend no busca cerrar un MVP comercial completo todavía. Su objetivo actual es servir como interfaz gráfica de prueba para:

- crear registros
- listarlos con paginación
- consultarlos por código
- modificarlos
- desactivarlos
- reactivarlos en la vista de eliminados

La referencia de comportamiento base es `Categoria`, y el resto de módulos debe respetar ese patrón con las particularidades de cada entidad.

## Convenciones generales de la API

- Base URL local habitual: `http://localhost:8080`
- Las APIs usan `codigo` de negocio. No deben usarse `id` internos.
- La mayoría de los módulos tienen `siguiente-codigo` para sugerir el próximo código al crear.
- Los listados usan paginación Spring.
- Los listados devuelven por defecto solo registros activos salvo que se envíe `activo=false`.
- La búsqueda por `nombre` requiere mínimo 2 caracteres.
- La desactivación es baja lógica. No hay borrado físico desde frontend.
- La reactivación se hace con un endpoint `PATCH .../activar`.
- El frontend debe mostrar errores del backend usando `status`, `codigo`, `mensaje`, `detalle` y `errores`.

## Formato de error

Respuesta de error estándar:

```json
{
  "status": 409,
  "error": "Conflict",
  "codigo": "GEN-409-001",
  "mensaje": "Ese codigo ya existe",
  "detalle": "Ya existe un codigo de barra: 7791234567890",
  "errores": null,
  "timestamp": "2026-03-25T09:40:12.3456789"
}
```

Errores frecuentes:

- `400 Bad Request`: validación, parámetros inválidos, JSON mal formado
- `404 Not Found`: recurso inexistente
- `409 Conflict`: código duplicado o conflictos de unicidad
- `500 Internal Server Error`: error inesperado

## Formato de paginación

Los listados paginados son `Page<T>` de Spring. El frontend debe esperar un JSON similar a este:

```json
{
  "content": [],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 1,
  "totalElements": 12,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false
  },
  "first": true,
  "numberOfElements": 10,
  "empty": false
}
```

Parámetros comunes de listado:

- `page`
- `size`
- `sort`
- `activo`
- `nombre`

## Patrón de pantallas base

Patrón general esperado para `Categoria`, `Proveedor`, `Impuesto`, `ListaPrecio` y `Sucursal`:

1. Pantalla principal de activos
- grid paginado
- búsqueda por nombre
- filtro implícito `activo=true`
- botón `Nuevo`
- botón `Editar`
- botón `Eliminar` que hace baja lógica

2. Pantalla de alta
- formulario de creación
- precargar `siguiente-codigo` como sugerencia editable

3. Pantalla de eliminados
- grid paginado con `activo=false`
- botón `Activar`

4. Pantalla de edición
- cargar por `codigo`
- guardar cambios con `PUT`

## Modulo Categoria

### Endpoints

- `GET /api/categorias/siguiente-codigo`
- `GET /api/categorias?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/categorias/codigo/{codigo}`
- `POST /api/categorias`
- `PUT /api/categorias/{codigo}`
- `DELETE /api/categorias/{codigo}`
- `PATCH /api/categorias/{codigo}/activar`

### DTO lectura

```json
{
  "codigo": 101,
  "nombre": "Bebidas",
  "descripcion": "Categoria de bebidas",
  "fechaCreacion": "2026-03-25T10:00:00",
  "fechaModificacion": "2026-03-25T10:00:00"
}
```

### DTO creación

```json
{
  "codigo": 101,
  "nombre": "Bebidas",
  "descripcion": "Categoria de bebidas"
}
```

## Modulo Proveedor

### Endpoints

- `GET /api/proveedores/siguiente-codigo`
- `GET /api/proveedores?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/proveedores/codigo/{codigo}`
- `POST /api/proveedores`
- `PUT /api/proveedores/{codigo}`
- `DELETE /api/proveedores/{codigo}`
- `PATCH /api/proveedores/{codigo}/activar`

### DTO

```json
{
  "codigo": 201,
  "nombre": "Proveedor Norte",
  "contacto": "Ana",
  "telefono": "3410000000",
  "email": "ana@proveedor.com",
  "direccion": "Calle 123",
  "activo": true
}
```

## Modulo Impuesto

### Endpoints

- `GET /api/impuestos/siguiente-codigo`
- `GET /api/impuestos?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/impuestos/codigo/{codigo}`
- `POST /api/impuestos`
- `PUT /api/impuestos/codigo/{codigo}`
- `DELETE /api/impuestos/codigo/{codigo}`
- `PATCH /api/impuestos/codigo/{codigo}/activar`

### DTO

```json
{
  "codigo": 21,
  "nombre": "IVA 21%",
  "descripcion": "Impuesto general",
  "porcentaje": 21.0000,
  "activo": true,
  "fechaCreacion": "2026-03-25T10:00:00",
  "fechaModificacion": "2026-03-25T10:00:00"
}
```

## Modulo ListaPrecio

### Endpoints

- `GET /api/listas-precios/siguiente-codigo`
- `GET /api/listas-precios?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/listas-precios/codigo/{codigo}`
- `POST /api/listas-precios`
- `PUT /api/listas-precios/codigo/{codigo}`
- `DELETE /api/listas-precios/codigo/{codigo}`
- `PATCH /api/listas-precios/codigo/{codigo}/activar`

### DTO

```json
{
  "codigo": 100,
  "nombre": "Minorista",
  "descripcion": "Precio de venta minorista",
  "activo": true,
  "fechaCreacion": "2026-03-25T10:00:00",
  "fechaModificacion": "2026-03-25T10:00:00"
}
```

## Modulo Sucursal

### Endpoints

- `GET /api/sucursal/siguiente-codigo`
- `GET /api/sucursal?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/sucursal/codigo/{codigo}`
- `POST /api/sucursal`
- `PUT /api/sucursal/{codigo}`
- `DELETE /api/sucursal/{codigo}`
- `PATCH /api/sucursal/{codigo}/activar`

### DTO

```json
{
  "codigo": 1,
  "nombre": "Casa Central",
  "direccion": "Av. Principal 100",
  "telefono": "3411111111",
  "encargado": "Carlos",
  "creado": "2026-03-25T10:00:00",
  "modificado": "2026-03-25T10:00:00",
  "creador": null,
  "modificador": null
}
```

## Modulo Producto Completo

Este es el flujo compuesto principal para artículos. No reemplaza todos los endpoints de `producto`, pero sí concentra el CRUD operativo principal para trabajar con el artículo completo.

### Qué incluye

- datos base de producto
- costo compuesto
- códigos de barra
- componentes de costo
- precios por lista
- stock opcional por sucursal en lectura
- creación automática de stock para todas las sucursales activas al dar de alta
- reserva inicial global opcional al crear
- edición de reserva por sucursal al modificar
- validación completa de coherencia de cálculo
- confirmación explícita para precios por debajo del costo

### Qué no hace todavía

- no hace movimientos de stock
- no hace ventas ni compras
- no maneja autenticación ni contexto real de usuario/sucursal

### Endpoints

- `GET /api/productos/completo?codigo=&sucursal=&nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/productos/completo/codigo/{codigo}?sucursal=1`
- `POST /api/productos/completo`
- `PUT /api/productos/completo/codigo/{codigo}`

### Reglas de listado

- si no se envía `nombre`, lista paginada normal
- si se envía `nombre`, debe tener al menos 2 caracteres
- `activo=true` por defecto
- `codigo` es un filtro exacto opcional
- `sucursal` es opcional y solo sirve para enriquecer la respuesta con el `stock` de esa sucursal

### DTO de alta

```json
{
  "producto": {
    "codigo": 9001,
    "nombre": "Yerba Mate Suave 1kg",
    "descripcion": "Paquete de yerba mate",
    "categoria": 101,
    "proveedor": 201,
    "manejaStock": true,
    "activo": true
  },
  "costo": {
    "costoBase": 2200.0000,
    "costoFinal": 2824.1400,
    "moneda": "ARS",
    "componentes": [
      {
        "ordenAplicacion": 1,
        "concepto": "MERMA",
        "codigoReferencia": null,
        "tipoValor": "PORCENTAJE",
        "valor": 2.0000,
        "subtotalAnterior": 2200.0000,
        "importeCalculado": 44.0000,
        "subtotalResultante": 2244.0000
      },
      {
        "ordenAplicacion": 2,
        "concepto": "EMBALAJE",
        "codigoReferencia": null,
        "tipoValor": "FIJO",
        "valor": 90.0000,
        "subtotalAnterior": 2244.0000,
        "importeCalculado": 90.0000,
        "subtotalResultante": 2334.0000
      },
      {
        "ordenAplicacion": 3,
        "concepto": "IVA",
        "codigoReferencia": 21,
        "tipoValor": "PORCENTAJE",
        "valor": 21.0000,
        "subtotalAnterior": 2334.0000,
        "importeCalculado": 490.1400,
        "subtotalResultante": 2824.1400
      }
    ]
  },
  "reservaInicial": 2.0,
  "codigosBarra": [
    "7791234567890"
  ],
  "precios": [
    {
      "listaPrecio": 100,
      "margenPorcentaje": 30.0000,
      "precioVenta": 3671.38
    }
  ],
  "confirmarBajoCosto": false
}
```

### DTO de modificación

```json
{
  "producto": {
    "nombre": "Yerba Mate Suave 1kg",
    "descripcion": "Paquete actualizado",
    "categoria": 101,
    "proveedor": 201,
    "manejaStock": true,
    "activo": true
  },
  "costo": {
    "costoBase": 2200.0000,
    "costoFinal": 2824.1400,
    "moneda": "ARS",
    "componentes": [
      {
        "ordenAplicacion": 1,
        "concepto": "MERMA",
        "codigoReferencia": null,
        "tipoValor": "PORCENTAJE",
        "valor": 2.0000,
        "subtotalAnterior": 2200.0000,
        "importeCalculado": 44.0000,
        "subtotalResultante": 2244.0000
      },
      {
        "ordenAplicacion": 2,
        "concepto": "EMBALAJE",
        "codigoReferencia": null,
        "tipoValor": "FIJO",
        "valor": 90.0000,
        "subtotalAnterior": 2244.0000,
        "importeCalculado": 90.0000,
        "subtotalResultante": 2334.0000
      },
      {
        "ordenAplicacion": 3,
        "concepto": "IVA",
        "codigoReferencia": 21,
        "tipoValor": "PORCENTAJE",
        "valor": 21.0000,
        "subtotalAnterior": 2334.0000,
        "importeCalculado": 490.1400,
        "subtotalResultante": 2824.1400
      }
    ]
  },
  "sucursal": 1,
  "reserva": 3.0,
  "codigosBarra": [
    "7791234567890"
  ],
  "precios": [
    {
      "listaPrecio": 100,
      "margenPorcentaje": 30.0000,
      "precioVenta": 3671.38
    }
  ],
  "confirmarBajoCosto": false
}
```

### DTO de lectura detallada

```json
{
  "producto": {
    "codigo": 9001,
    "nombre": "Yerba Mate Suave 1kg",
    "descripcion": "Paquete de yerba mate",
    "categoria": 101,
    "proveedor": 201,
    "manejaStock": true,
    "activo": true,
    "fechaCreacion": "2026-03-25T10:00:00",
    "fechaModificacion": "2026-03-25T10:00:00"
  },
  "costo": {
    "costoBase": 2200.0000,
    "costoFinal": 2824.1400,
    "moneda": "ARS",
    "componentes": [
      {
        "ordenAplicacion": 1,
        "concepto": "MERMA",
        "codigoReferencia": null,
        "tipoValor": "PORCENTAJE",
        "valor": 2.0000,
        "subtotalAnterior": 2200.0000,
        "importeCalculado": 44.0000,
        "subtotalResultante": 2244.0000
      },
      {
        "ordenAplicacion": 2,
        "concepto": "EMBALAJE",
        "codigoReferencia": null,
        "tipoValor": "FIJO",
        "valor": 90.0000,
        "subtotalAnterior": 2244.0000,
        "importeCalculado": 90.0000,
        "subtotalResultante": 2334.0000
      },
      {
        "ordenAplicacion": 3,
        "concepto": "IVA",
        "codigoReferencia": 21,
        "tipoValor": "PORCENTAJE",
        "valor": 21.0000,
        "subtotalAnterior": 2334.0000,
        "importeCalculado": 490.1400,
        "subtotalResultante": 2824.1400
      }
    ]
  },
  "codigosBarra": [
    "7791234567890"
  ],
  "stock": {
    "codigo": 9001,
    "sucursal": 1,
    "cantidad": 0.0,
    "reserva": 2.0
  },
  "precios": [
    {
      "listaPrecio": 100,
      "costoFinalReferencia": 2824.1400,
      "margenPorcentaje": 30.0000,
      "precioVenta": 3671.38,
      "debajoCosto": false
    }
  ]
}
```

### DTO resumen de listado

El listado devuelve una versión resumida:

```json
{
  "producto": {
    "codigo": 9001,
    "nombre": "Yerba Mate Suave 1kg",
    "descripcion": "Paquete de yerba mate",
    "categoria": 101,
    "proveedor": 201,
    "manejaStock": true,
    "activo": true,
    "fechaCreacion": "2026-03-25T10:00:00",
    "fechaModificacion": "2026-03-25T10:00:00"
  },
  "costoFinal": 2824.1400,
  "moneda": "ARS",
  "stock": {
    "codigo": 9001,
    "sucursal": 1,
    "cantidad": 0.0,
    "reserva": 2.0
  },
  "precios": [
    {
      "listaPrecio": 100,
      "costoFinalReferencia": 2824.1400,
      "margenPorcentaje": 30.0000,
      "precioVenta": 3671.38,
      "debajoCosto": false
    }
  ]
}
```

### Reglas importantes de producto completo

- `categoria` y `proveedor` hoy pueden ser `null`
- `precios` es obligatorio y debe tener al menos un elemento
- al crear, el sistema genera stock para todas las sucursales activas
- `reservaInicial` se replica en todos los stocks creados
- al modificar, si se quiere tocar la reserva, se debe enviar `sucursal` y `reserva`
- si se consulta con `sucursal`, la respuesta incluye el `stock` de esa sucursal
- si no se envía `sucursal`, `stock` puede venir `null`
- el backend recalcula todo desde `costo.costoBase`
- el frontend debe enviar `costoFinal`, componentes y precios ya recalculados, pero el backend los vuelve a validar
- los componentes del costo deben enviarse en orden
- un componente con `codigoReferencia` debe coincidir con el valor real del catálogo referenciado, por ejemplo un impuesto
- la política de redondeo acordada es:
  - intermedios: 4 decimales
  - precio final de venta: 2 decimales
  - tolerancia de comparación: `0.01`
- si el backend detecta incoherencia, responde error `PREC-409-001`
- si una lista queda por debajo del costo y `confirmarBajoCosto=false`, responde error `PREC-409-002`
- `confirmarBajoCosto` es solo un flag transitorio del request, no forma parte del modelo persistido
- la respuesta de lectura incluye `debajoCosto` por lista para que el frontend pueda destacarlo visualmente

### Cálculo esperado en frontend

El frontend debe recalcular en caliente con la misma lógica del backend:

1. partir de `costoBase`
2. aplicar componentes en `ordenAplicacion`
3. si `tipoValor = PORCENTAJE`, calcular:
   - `importeCalculado = subtotalAnterior * valor / 100`
4. si `tipoValor = FIJO`, calcular:
   - `importeCalculado = valor`
5. calcular:
   - `subtotalResultante = subtotalAnterior + importeCalculado`
6. el último `subtotalResultante` es `costoFinal`
7. por cada lista:
   - `precioVenta = costoFinal * (1 + margenPorcentaje / 100)`

Redondeo esperado:

- `subtotalAnterior`, `importeCalculado`, `subtotalResultante`, `costoFinal`, `margenPorcentaje`: 4 decimales
- `precioVenta`: 2 decimales

### Flujo de precio bajo costo

Si el backend responde que una lista está bajo costo:

1. el frontend debe mostrar un modal de confirmación
2. mensaje sugerido:
   - `El precio de venta es menor al costo. Seguro de guardar asi?`
3. si el operador acepta:
   - reenviar el mismo request con `confirmarBajoCosto=true`
4. si el operador cancela:
   - no guardar y permitir corrección

La bandera relevante para este flujo es:

```json
{
  "confirmarBajoCosto": true
}
```

Y la respuesta de lectura por lista expone:

```json
{
  "debajoCosto": true
}
```

Esto debe servir al frontend para:

- marcar visualmente precios riesgosos
- pedir confirmación al guardar
- y rediseñar formularios y grillas con esta señal operativa

## Modulo Stock

Por ahora es un módulo de consulta puntual, no de gestión operativa desde frontend.

### Endpoint

- `GET /api/stock?codigo=9001&sucursal=1`

### Respuesta

```json
{
  "codigo": 9001,
  "sucursal": 1,
  "cantidad": 0.0,
  "reserva": 2.0
}
```

## Pantallas sugeridas

### Pantallas maestras

Aplicar el patrón a:

- Categorias
- Proveedores
- Impuestos
- Listas de precios
- Sucursales

Cada pantalla debería tener:

- pestaña o vista de activos
- pestaña o vista de eliminados
- búsqueda por nombre
- paginación
- botón `Nuevo`
- acción `Editar`
- acción `Eliminar` o `Activar` según la vista

### Pantalla de productos

El frontend de artículos debería tener como mínimo:

1. Padrón de artículos
- grid paginado
- búsqueda por nombre
- filtro opcional por código
- mostrar datos base, costo y precios
- opcionalmente stock si hay sucursal seleccionada
- botón `Nuevo`
- botón `Editar`
- botón `Desactivar`

2. Alta de artículo completo
- usar formularios para producto, costo, códigos de barra, impuestos, listas de precios
- permitir `categoria` y `proveedor` nulos por ahora
- usar catálogos cargados desde backend

3. Edición de artículo completo
- cargar por código
- permitir modificar reserva indicando sucursal

4. Vista de eliminados
- equivalente a la de activos, pero con `activo=false`
- debe usar activación/desactivación desde el módulo base `producto`
- aunque la pantalla sea de `productoCompleto`, el cambio de estado se resuelve contra `producto`

## Decisiones de UI ya asumidas en este documento

- el backend resuelve la lógica de negocio, composición de datos, filtros y paginación
- el frontend solo consume y presenta
- los botones `Eliminar` hacen baja lógica
- los códigos sugeridos pueden editarse manualmente
- los formularios deben mostrar validaciones del backend
- el frontend debe usar `codigo` como identificador principal
- la UI no tendrá login por ahora
- habrá un selector global de sucursal activa para las pantallas de productos
- el padrón de productos será siempre paginado; no se cargará todo en memoria del frontend
- la interfaz buscada es administrativa tipo ERP, no una landing ni una UI minimalista
- la desactivación y reactivación de artículos se hará usando el módulo base `producto`
- la navegación general será lateral
- la grilla de productos mostrará una vista resumida con los datos importantes
- el detalle completo del producto quedará para consulta puntual y edición

## Decisiones ya cerradas para el frontend

1. Producto desactivado
- la UI de `productoCompleto` debe usar activación/desactivación del módulo base `producto`

2. Sucursal activa
- la UI debe tener un selector global de sucursal activa
- ese valor debe enviarse en las consultas de `productoCompleto` cuando haga falta enriquecer la respuesta con stock y reserva

3. Padrón de productos
- el listado será paginado
- no debe cargarse el padrón completo en memoria del frontend

4. Autenticación
- no habrá login por ahora
- el frontend es solo de prueba funcional

5. Estilo visual
- se busca una interfaz tipo ERP
- densa, operativa y pensada para gestión

6. Navegación general
- la aplicación debe usar navegación lateral por módulos

7. Grilla de productos
- debe mostrar una vista resumida
- la edición y consulta puntual usarán la vista completa del producto

## Resumen operativo para la otra IA

Si la IA genera el frontend ahora mismo, debería empezar por este orden:

1. crear cliente HTTP común
2. crear manejo global de errores
3. implementar patrón CRUD maestro reutilizable
4. montar pantallas de `Categoria`
5. replicar el patrón en `Proveedor`, `Impuesto`, `ListaPrecio` y `Sucursal`
6. montar `ProductoCompleto` con listado, alta, edición y detalle
7. agregar selector de sucursal si se quiere mostrar o editar reserva/stock
