# Resumen Completo para Construir el Frontend

Este archivo describe el backend actual y todo lo que el frontend debe hacer para operar correctamente.

No debes inventar endpoints, campos ni reglas de negocio fuera de lo documentado aqui.

## Objetivo del frontend

El frontend actual debe servir como interfaz administrativa de prueba para operar el backend.

Debe permitir:

- consultar maestros
- crear y modificar maestros
- desactivar y reactivar donde exista esa capacidad
- consultar y operar `productoCompleto`
- mostrar stock por sucursal
- validar y recalcular costos/precios del lado cliente para asistir al operador
- respetar siempre la validacion final del backend

No hay login ni permisos en esta etapa.

## Datos tecnicos base

- Base URL local habitual: `http://localhost:8080`
- CORS habilitado para `http://localhost:5173`
- Todas las APIs usan JSON
- El identificador funcional principal es `codigo`
- No usar `id` internos en frontend

## Convenciones generales de API

- los listados paginados usan `Page<T>` de Spring
- por defecto los listados usan `activo=true`
- si hay filtro por `nombre`, el backend exige minimo 2 caracteres
- las bajas son logicas
- la reactivacion se hace con `PATCH .../activar`
- el frontend debe mostrar mensajes del backend y no taparlos con validaciones inventadas

## Formato general de error

El backend devuelve errores con una estructura tipo:

```json
{
  "status": 409,
  "error": "Conflict",
  "codigo": "GEN-409-001",
  "mensaje": "Ese codigo ya existe",
  "detalle": "Ya existe un componente de costo con codigo: 101",
  "errores": null,
  "timestamp": "2026-03-26T16:00:00.0000000"
}
```

El frontend debe mostrar:

- `mensaje` como texto principal
- `detalle` como ampliacion
- `errores` campo por campo si existe

## Formato general de paginacion

Los listados paginados devuelven algo como:

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

Parametros comunes:

- `page`
- `size`
- `sort`
- `activo`
- `nombre`

## Maestros disponibles

Los maestros principales para el frontend son:

- `categoria`
- `proveedor`
- `sucursal`
- `listaPrecio`
- `componenteCosto`

Patron sugerido de UI para todos ellos:

1. vista de activos
2. vista de inactivos
3. buscador por nombre
4. paginacion
5. boton `Nuevo`
6. boton `Editar`
7. boton `Eliminar`
8. boton `Activar` en la vista de inactivos

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
  "codigo": 100,
  "nombre": "Almacen",
  "descripcion": "Productos secos y de consumo diario",
  "fechaCreacion": "2026-03-26T16:00:00",
  "fechaModificacion": "2026-03-26T16:00:00"
}
```

### DTO alta

```json
{
  "codigo": 100,
  "nombre": "Almacen",
  "descripcion": "Productos secos y de consumo diario"
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

### DTO lectura

```json
{
  "codigo": 1000,
  "nombre": "Molinos del Rio",
  "contacto": "Ventas",
  "telefono": "3415001000",
  "email": "ventas@molinosdelrio.com",
  "direccion": "Av. Industrial 1250",
  "activo": true
}
```

### DTO alta

```json
{
  "codigo": 1000,
  "nombre": "Molinos del Rio",
  "contacto": "Ventas",
  "telefono": "3415001000",
  "email": "ventas@molinosdelrio.com",
  "direccion": "Av. Industrial 1250"
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

### DTO lectura

```json
{
  "codigo": 1,
  "nombre": "Casa Central",
  "direccion": "Bv. Principal 100",
  "telefono": "3414100001",
  "encargado": "Ana Gomez",
  "creado": "2026-03-26T16:00:00",
  "modificado": "2026-03-26T16:00:00",
  "creador": "seed",
  "modificador": "seed"
}
```

### DTO alta

```json
{
  "codigo": 1,
  "nombre": "Casa Central",
  "direccion": "Bv. Principal 100",
  "telefono": "3414100001",
  "encargado": "Ana Gomez"
}
```

## Modulo Lista de Precios

### Endpoints

- `GET /api/listas-precios/siguiente-codigo`
- `GET /api/listas-precios?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/listas-precios/codigo/{codigo}`
- `POST /api/listas-precios`
- `PUT /api/listas-precios/codigo/{codigo}`
- `DELETE /api/listas-precios/codigo/{codigo}`
- `PATCH /api/listas-precios/codigo/{codigo}/activar`

### DTO lectura

```json
{
  "codigo": 100,
  "nombre": "Minorista",
  "descripcion": "Precio de mostrador",
  "activo": true,
  "fechaCreacion": "2026-03-26T16:00:00",
  "fechaModificacion": "2026-03-26T16:00:00"
}
```

### DTO alta

```json
{
  "codigo": 100,
  "nombre": "Minorista",
  "descripcion": "Precio de mostrador",
  "activo": true
}
```

## Modulo Componente de Costo

Este modulo reemplaza el viejo concepto de impuesto aislado.

Representa cualquier componente que participe en el costo:

- impuesto
- gasto
- recargo
- ajuste

### Endpoints

- `GET /api/componentes-costo/siguiente-codigo`
- `GET /api/componentes-costo?nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/componentes-costo/codigo/{codigo}`
- `POST /api/componentes-costo`
- `PUT /api/componentes-costo/codigo/{codigo}`
- `DELETE /api/componentes-costo/codigo/{codigo}`
- `PATCH /api/componentes-costo/codigo/{codigo}/activar`

### DTO lectura

```json
{
  "codigo": 101,
  "nombre": "Flete Logistico",
  "descripcion": "Costo fijo de traslado y recepcion",
  "tipoComponente": "GASTO",
  "tipoValor": "FIJO",
  "valorDefecto": 120.0000,
  "modoBase": "COSTO_BASE",
  "prioridadAplicacion": 1,
  "editableEnProducto": true,
  "obligatorio": true,
  "activo": true,
  "fechaCreacion": "2026-03-26T16:00:00",
  "fechaModificacion": "2026-03-26T16:00:00"
}
```

### DTO alta/modificacion

```json
{
  "codigo": 101,
  "nombre": "Flete Logistico",
  "descripcion": "Costo fijo de traslado y recepcion",
  "tipoComponente": "GASTO",
  "tipoValor": "FIJO",
  "valorDefecto": 120.0000,
  "modoBase": "COSTO_BASE",
  "prioridadAplicacion": 1,
  "editableEnProducto": true,
  "obligatorio": true,
  "activo": true
}
```

### Reglas importantes para frontend

- `tipoValor` hoy puede ser `FIJO` o `PORCENTAJE`
- `modoBase` hoy soporta:
  - `COSTO_BASE`
  - `SUBTOTAL_ACUMULADO`
  - `SUBTOTAL_SIN_IVA`
  - `SUBTOTAL_ANTES_DE_IMPUESTOS_FINALES`
- `prioridadAplicacion` define el orden real del calculo
- si `editableEnProducto = false`, el operador no deberia poder cambiar el valor aplicado en el producto
- si `obligatorio = true`, el frontend deberia incluirlo automaticamente en formularios de producto completo

## Modulo Producto Base

Este modulo no es el flujo principal de carga completa, pero sigue vigente para algunas operaciones base.

### Endpoints

- `GET /api/productos/siguiente-codigo`
- `GET /api/productos`
- `GET /api/productos/codigo/{codigo}`
- `PUT /api/productos/codigo/{codigo}`

### Observaciones

- `GET /api/productos` devuelve lista simple, no paginada
- hoy no existen endpoints de desactivar/activar producto en este controller
- por eso el frontend no debe asumir que puede gestionar estado de producto desde aqui

### DTO lectura

```json
{
  "codigo": 9001,
  "nombre": "Yerba Mate Suave 1kg",
  "descripcion": "Yerba mate estacionada molienda suave",
  "categoria": 100,
  "proveedor": 1000,
  "manejaStock": true,
  "activo": true,
  "fechaCreacion": "2026-03-26T16:00:00",
  "fechaModificacion": "2026-03-26T16:00:00"
}
```

### DTO alta base

```json
{
  "codigo": 9001,
  "nombre": "Yerba Mate Suave 1kg",
  "descripcion": "Yerba mate estacionada molienda suave",
  "categoria": 100,
  "proveedor": 1000,
  "manejaStock": true,
  "activo": true
}
```

### DTO modificacion base

```json
{
  "nombre": "Yerba Mate Suave 1kg",
  "descripcion": "Texto actualizado",
  "categoria": 100,
  "proveedor": 1000,
  "manejaStock": true,
  "activo": true
}
```

## Modulo Stock

Este modulo es de consulta puntual.

### Endpoint

- `GET /api/stock?codigo=9001&sucursal=1`

### DTO

```json
{
  "codigo": 9001,
  "sucursal": 1,
  "cantidad": 23.0,
  "reserva": 2.0
}
```

### Regla funcional

La identidad del stock es la combinacion:

- `codigo` de producto
- `sucursal`

## Modulo Producto Completo

Este es el flujo principal que el frontend debe usar para alta, listado, consulta y modificacion operativa del articulo completo.

Incluye:

- datos base de producto
- costo vigente
- detalle vigente del costo
- codigos de barra
- precios vigentes
- stock opcional de una sucursal
- reserva inicial al crear
- modificacion de reserva por sucursal

No incluye:

- historial
- movimientos de stock
- ventas
- compras
- autenticacion

### Endpoints

- `GET /api/productos/completo?codigo=&sucursal=&nombre=&activo=true&page=0&size=10&sort=nombre`
- `GET /api/productos/completo/codigo/{codigo}?sucursal=1`
- `POST /api/productos/completo`
- `PUT /api/productos/completo/codigo/{codigo}`

## Logica de consulta de `productoCompleto`

### Listado general

`GET /api/productos/completo`

Filtros:

- `codigo`
- `nombre`
- `activo`
- `page`
- `size`
- `sort`
- `sucursal`

Reglas:

- `activo=true` por defecto
- `nombre` requiere minimo 2 caracteres
- `codigo` en este endpoint es busqueda parcial por digitos
  - ejemplo: `55` puede devolver `55`, `155`, `1055`, `5500`
- `codigo` puede tener un solo digito
- `sucursal` es opcional
- si `sucursal` se envia, el backend intenta enriquecer cada item con stock de esa sucursal

### Consulta puntual

`GET /api/productos/completo/codigo/{codigo}`

Reglas:

- aca `codigo` es exacto
- `sucursal` sigue siendo opcional
- si `sucursal` se envia, la respuesta incluye el stock puntual de esa sucursal
- si no se envia `sucursal`, `stock` puede venir `null`

## DTO de lectura detallada de `productoCompleto`

```json
{
  "producto": {
    "codigo": 9012,
    "nombre": "Yogur Bebible Vainilla 1L",
    "descripcion": "Yogur bebible sabor vainilla",
    "categoria": 500,
    "proveedor": 5000,
    "manejaStock": true,
    "activo": true,
    "fechaCreacion": "2026-03-26T16:00:00",
    "fechaModificacion": "2026-03-26T16:00:00"
  },
  "costo": {
    "costoBase": 1540.0000,
    "costoFinal": 2051.3493,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "nombreComponente": "Flete Logistico",
        "ordenAplicado": 1,
        "tipoValorAplicado": "FIJO",
        "modoBaseAplicado": "COSTO_BASE",
        "valorAplicado": 98.0000,
        "baseCalculo": 1540.0000,
        "importeCalculado": 98.0000,
        "subtotalResultante": 1638.0000
      },
      {
        "componente": 102,
        "nombreComponente": "Ingresos Brutos",
        "ordenAplicado": 2,
        "tipoValorAplicado": "PORCENTAJE",
        "modoBaseAplicado": "SUBTOTAL_SIN_IVA",
        "valorAplicado": 3.5000,
        "baseCalculo": 1638.0000,
        "importeCalculado": 57.3300,
        "subtotalResultante": 1695.3300
      },
      {
        "componente": 103,
        "nombreComponente": "IVA 21",
        "ordenAplicado": 3,
        "tipoValorAplicado": "PORCENTAJE",
        "modoBaseAplicado": "SUBTOTAL_SIN_IVA",
        "valorAplicado": 21.0000,
        "baseCalculo": 1695.3300,
        "importeCalculado": 356.0193,
        "subtotalResultante": 2051.3493
      }
    ]
  },
  "codigosBarra": [
    "7791000000012"
  ],
  "stock": {
    "codigo": 9012,
    "sucursal": 1,
    "cantidad": 53.0,
    "reserva": 3.0
  },
  "precios": [
    {
      "listaPrecio": 100,
      "costoFinalReferencia": 2051.3493,
      "margenPorcentaje": 36.5000,
      "precioVenta": 2800.09
    },
    {
      "listaPrecio": 200,
      "costoFinalReferencia": 2051.3493,
      "margenPorcentaje": 21.0000,
      "precioVenta": 2482.13
    },
    {
      "listaPrecio": 300,
      "costoFinalReferencia": 2051.3493,
      "margenPorcentaje": 30.5000,
      "precioVenta": 2677.01
    }
  ]
}
```

## DTO resumen de listado de `productoCompleto`

```json
{
  "producto": {
    "codigo": 9012,
    "nombre": "Yogur Bebible Vainilla 1L",
    "descripcion": "Yogur bebible sabor vainilla",
    "categoria": 500,
    "proveedor": 5000,
    "manejaStock": true,
    "activo": true,
    "fechaCreacion": "2026-03-26T16:00:00",
    "fechaModificacion": "2026-03-26T16:00:00"
  },
  "costoFinal": 2051.3493,
  "moneda": "ARS",
  "stock": {
    "codigo": 9012,
    "sucursal": 1,
    "cantidad": 53.0,
    "reserva": 3.0
  },
  "precios": [
    {
      "listaPrecio": 100,
      "costoFinalReferencia": 2051.3493,
      "margenPorcentaje": 36.5000,
      "precioVenta": 2800.09
    }
  ]
}
```

## DTO de alta de `productoCompleto`

```json
{
  "producto": {
    "codigo": 9500,
    "nombre": "Cafe Tostado en Grano 500g",
    "descripcion": "Cafe de tueste medio",
    "categoria": 100,
    "proveedor": 1000,
    "manejaStock": true,
    "activo": true
  },
  "costo": {
    "costoBase": 3200.0000,
    "costoFinal": 4245.3600,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "valorAplicado": 130.0000,
        "baseCalculo": 3200.0000,
        "importeCalculado": 130.0000,
        "subtotalResultante": 3330.0000
      },
      {
        "componente": 102,
        "valorAplicado": 3.5000,
        "baseCalculo": 3330.0000,
        "importeCalculado": 116.5500,
        "subtotalResultante": 3446.5500
      },
      {
        "componente": 103,
        "valorAplicado": 21.0000,
        "baseCalculo": 3446.5500,
        "importeCalculado": 723.7755,
        "subtotalResultante": 4170.3255
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
      "margenPorcentaje": 35.0000,
      "precioVenta": 5655.99
    },
    {
      "listaPrecio": 200,
      "margenPorcentaje": 18.0000,
      "precioVenta": 5009.52
    },
    {
      "listaPrecio": 300,
      "margenPorcentaje": 28.0000,
      "precioVenta": 5433.07
    }
  ],
  "confirmarBajoCosto": false
}
```

## DTO de modificacion de `productoCompleto`

```json
{
  "producto": {
    "nombre": "Cafe Tostado en Grano 500g",
    "descripcion": "Cafe de tueste medio actualizado",
    "categoria": 100,
    "proveedor": 1000,
    "manejaStock": true,
    "activo": true
  },
  "costo": {
    "costoBase": 3300.0000,
    "costoFinal": 4344.6975,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "valorAplicado": 135.0000,
        "baseCalculo": 3300.0000,
        "importeCalculado": 135.0000,
        "subtotalResultante": 3435.0000
      },
      {
        "componente": 102,
        "valorAplicado": 3.5000,
        "baseCalculo": 3435.0000,
        "importeCalculado": 120.2250,
        "subtotalResultante": 3555.2250
      },
      {
        "componente": 103,
        "valorAplicado": 21.0000,
        "baseCalculo": 3555.2250,
        "importeCalculado": 746.5973,
        "subtotalResultante": 4301.8223
      }
    ]
  },
  "sucursal": 1,
  "reserva": 4.0,
  "codigosBarra": [
    "7791234567890"
  ],
  "precios": [
    {
      "listaPrecio": 100,
      "margenPorcentaje": 35.0000,
      "precioVenta": 5865.34
    },
    {
      "listaPrecio": 200,
      "margenPorcentaje": 18.0000,
      "precioVenta": 5126.74
    }
  ],
  "confirmarBajoCosto": false
}
```

## Reglas funcionales de `productoCompleto`

### Datos base de producto

- `categoria` puede ser `null`
- `proveedor` puede ser `null`
- `manejaStock` puede ser `true` o `false`
- `activo` puede enviarse

### Codigos de barra

- pueden omitirse
- si se envian, no puede haber repetidos dentro del request
- tampoco pueden existir en otro producto

### Componentes de costo

Cada componente del request representa:

- el componente maestro elegido
- el valor usado en ese producto
- la base que el frontend calculo
- el importe calculado
- el subtotal resultante

El backend:

- busca el componente maestro por `codigo`
- valida que exista y este activo
- ordena usando `prioridadAplicacion` del maestro
- no usa el orden enviado por frontend
- recalcula la base y el importe
- compara contra lo recibido

### Regla importante sobre semantica

El producto no define:

- orden de aplicacion
- tipo de valor
- modo base

Eso sale del maestro `componenteCosto`.

### Costos

El backend parte siempre de:

- `costo.costoBase`

Luego:

1. toma los componentes enviados
2. consulta su definicion maestra
3. ordena por `prioridadAplicacion`
4. calcula cada componente segun `modoBase`
5. obtiene `costoFinal`
6. compara contra `costo.costoFinal`

### Modos base soportados hoy

- `COSTO_BASE`
- `SUBTOTAL_ACUMULADO`
- `SUBTOTAL_SIN_IVA`
- `SUBTOTAL_ANTES_DE_IMPUESTOS_FINALES`

Implementacion actual:

- `SUBTOTAL_SIN_IVA` y `SUBTOTAL_ANTES_DE_IMPUESTOS_FINALES` hoy usan el subtotal acumulado excluyendo componentes que el backend identifica como IVA por nombre
- por eso, para el frontend actual, conviene no inventar semanticas nuevas y trabajar con los componentes que ya existen

### Precios

Cada precio del request incluye:

- `listaPrecio`
- `margenPorcentaje`
- `precioVenta`

El backend recalcula:

```text
precioVenta = costoFinal * (1 + margenPorcentaje / 100)
```

Reglas:

- `margenPorcentaje` puede ser negativo
- `precioVenta` no puede ser negativo
- puede haber venta bajo costo, pero requiere confirmacion

### Reserva y stock

Al crear:

- el backend crea stock para todas las sucursales activas
- `reservaInicial` se copia a cada una

Al modificar:

- si queres cambiar reserva, debes enviar `sucursal` y `reserva` juntos
- si uno falta, el backend rechaza la operacion

### Confirmacion de precio bajo costo

Si alguna lista queda con `precioVenta < costoFinal`:

- si `confirmarBajoCosto` es `false` o `null`, el backend rechaza
- si `confirmarBajoCosto` es `true`, permite guardar

Flujo esperado de frontend:

1. guardar normal
2. si backend responde conflicto de confirmacion, mostrar modal
3. si operador acepta, reenviar mismo request con `confirmarBajoCosto: true`

## Politica de redondeo

El frontend debe usar esta misma politica para evitar rechazos por inconsistencia:

- calculos intermedios: 4 decimales
- `costoFinal`: 4 decimales
- `margenPorcentaje`: 4 decimales
- `precioVenta`: 2 decimales
- redondeo: `HALF_UP`
- tolerancia de comparacion backend: `0.01`

## Codigos de error funcionales importantes para frontend

- conflicto de calculo inconsistente:
  - `PREC-409-001`
- confirmacion requerida por precio bajo costo:
  - `PREC-409-002`

El frontend debe tratar ambos como errores manejables de negocio, no como errores tecnicos.

## Que datos utiles existen hoy en la base seed

La base actual de desarrollo ya tiene:

- 30 productos
- 3 sucursales
- 3 listas de precio
- 3 componentes de costo

Sucursales actuales:

- `1` Casa Central
- `2` Sucursal Centro
- `3` Sucursal Norte

Listas de precio actuales:

- `100` Minorista
- `200` Mayorista
- `300` Online

Componentes de costo actuales:

- `101` Flete Logistico
- `102` Ingresos Brutos
- `103` IVA 21

Productos de ejemplo disponibles:

- `9001` Yerba Mate Suave 1kg
- `9012` Yogur Bebible Vainilla 1L
- `9029` Alimento Balanceado Perro 15kg
- `9030` Auriculares Bluetooth In Ear

## Lo que el frontend debe hacer para ser funcional

### 1. Infraestructura base

- cliente HTTP centralizado
- manejo global de errores
- utilidades para `Page<T>`
- selector global de sucursal activa

### 2. Modulos maestros

Implementar pantallas completas para:

- categorias
- proveedores
- sucursales
- listas de precios
- componentes de costo

Cada una con:

- listado paginado
- alta
- edicion
- inactivos
- reactivacion

### 3. Pantalla de catalogo de productos

Usar `GET /api/productos/completo`.

La grilla debe mostrar como minimo:

- codigo
- nombre
- categoria
- proveedor
- costo final
- moneda
- precios
- stock si hay sucursal seleccionada
- activo

Filtros recomendados:

- codigo parcial
- nombre
- activos / inactivos
- sucursal seleccionada

### 4. Pantalla de alta de producto completo

Debe permitir:

- cargar datos base del producto
- elegir componentes de costo
- editar valor aplicado si corresponde
- ingresar codigos de barra
- definir precios por lista
- definir reserva inicial
- ver recalculo en vivo

Catálogos que debe consumir:

- categorias
- proveedores
- componentes de costo
- listas de precios

### 5. Pantalla de edicion de producto completo

Debe:

- cargar el detalle por codigo
- mostrar costo vigente y detalle
- permitir recalcular
- permitir editar reserva por sucursal
- reenviar request completo actualizado

### 6. Modal de confirmacion bajo costo

Obligatorio para no romper flujo de guardado.

### 7. Consulta de stock puntual

Puede resolverse:

- directamente con `GET /api/stock`
- o reutilizando `productoCompleto` con `sucursal`

## Orden recomendado de construccion del frontend

1. cliente HTTP y manejo global de errores
2. layout ERP con sidebar
3. CRUD reutilizable para maestros
4. categorias
5. proveedores
6. sucursales
7. listas de precios
8. componentes de costo
9. catalogo de productos completos
10. alta de producto completo
11. edicion de producto completo
12. modal de confirmacion bajo costo

## Regla final para la IA que construya el frontend

El frontend debe asumir que el backend manda.

Eso significa:

- no inventar reglas de negocio
- no recalcular distinto del backend
- no asumir campos que no existen
- no depender de `id`
- usar `codigo`
- tratar `productoCompleto` como flujo principal del articulo
