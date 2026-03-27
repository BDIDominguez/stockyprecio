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
  "nombre": "Merma Operativa",
  "descripcion": "Merma porcentual de primera etapa",
  "tipoComponente": "MERMA",
  "tipoValor": "PORCENTAJE",
  "valorDefecto": 1.5000,
  "etapaAplicacion": "ETAPA_1",
  "editableEnProducto": true,
  "activo": true,
  "fechaCreacion": "2026-03-26T16:00:00",
  "fechaModificacion": "2026-03-26T16:00:00"
}
```

### DTO alta/modificacion

```json
{
  "codigo": 101,
  "nombre": "Merma Operativa",
  "descripcion": "Merma porcentual de primera etapa",
  "tipoComponente": "MERMA",
  "tipoValor": "PORCENTAJE",
  "valorDefecto": 1.5000,
  "etapaAplicacion": "ETAPA_1",
  "editableEnProducto": true,
  "activo": true
}
```

### Reglas importantes para frontend

- `tipoValor` hoy puede ser `FIJO` o `PORCENTAJE`
- `etapaAplicacion` hoy soporta:
  - `ETAPA_1`
  - `ETAPA_2`
  - `ETAPA_FINAL`
- si `editableEnProducto = false`, el operador no deberia poder cambiar el valor aplicado en el producto
- si un componente esta en uso por algun producto, el backend no deja desactivarlo
- no existe obligatoriedad global de IVA ni de ningun otro componente

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
    "costoBase": 289.1852,
    "neto1": 292.0771,
    "neto2": 390.8394,
    "costoFinal": 390.8394,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "nombreComponente": "Merma Operativa",
        "etapaAplicacion": "ETAPA_1",
        "resultadoEtapa": "NETO1",
        "tipoValorAplicado": "PORCENTAJE",
        "valorAplicado": 1.0000,
        "baseCalculo": 289.1852,
        "importeCalculado": 2.8919,
        "subtotalResultante": 292.0771
      },
      {
        "componente": 103,
        "nombreComponente": "Flete Logistico",
        "etapaAplicacion": "ETAPA_2",
        "resultadoEtapa": "NETO2",
        "tipoValorAplicado": "FIJO",
        "valorAplicado": 90.0000,
        "baseCalculo": 292.0771,
        "importeCalculado": 90.0000,
        "subtotalResultante": 382.0771
      },
      {
        "componente": 104,
        "nombreComponente": "Ingresos Brutos",
        "etapaAplicacion": "ETAPA_2",
        "resultadoEtapa": "NETO2",
        "tipoValorAplicado": "PORCENTAJE",
        "valorAplicado": 3.0000,
        "baseCalculo": 292.0771,
        "importeCalculado": 8.7623,
        "subtotalResultante": 390.8394
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
      "costoFinalReferencia": 390.8394,
      "margenPorcentaje": 35.0000,
      "precioVenta": 527.63
    },
    {
      "listaPrecio": 200,
      "costoFinalReferencia": 390.8394,
      "margenPorcentaje": 22.0000,
      "precioVenta": 476.82
    },
    {
      "listaPrecio": 300,
      "costoFinalReferencia": 390.8394,
      "margenPorcentaje": 18.0000,
      "precioVenta": 461.19
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
  "costoFinal": 390.8394,
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
      "costoFinalReferencia": 390.8394,
      "margenPorcentaje": 35.0000,
      "precioVenta": 527.63
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
    "neto1": 3168.0000,
    "neto2": 3383.8800,
    "costoFinal": 4094.4948,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "valorAplicado": 1.5000,
        "baseCalculo": 3200.0000,
        "importeCalculado": 48.0000,
        "subtotalResultante": 3248.0000
      },
      {
        "componente": 102,
        "valorAplicado": -2.5000,
        "baseCalculo": 3200.0000,
        "importeCalculado": -80.0000,
        "subtotalResultante": 3168.0000
      },
      {
        "componente": 103,
        "valorAplicado": 105.0000,
        "baseCalculo": 3168.0000,
        "importeCalculado": 105.0000,
        "subtotalResultante": 3273.0000
      },
      {
        "componente": 104,
        "valorAplicado": 3.5000,
        "baseCalculo": 3168.0000,
        "importeCalculado": 110.8800,
        "subtotalResultante": 3383.8800
      },
      {
        "componente": 106,
        "valorAplicado": 21.0000,
        "baseCalculo": 3383.8800,
        "importeCalculado": 710.6148,
        "subtotalResultante": 4094.4948
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
      "precioVenta": 5527.57
    },
    {
      "listaPrecio": 200,
      "margenPorcentaje": 22.0000,
      "precioVenta": 4995.28
    },
    {
      "listaPrecio": 300,
      "margenPorcentaje": 18.0000,
      "precioVenta": 4831.50
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
    "neto1": 3349.5000,
    "neto2": 3616.6840,
    "costoFinal": 4376.1876,
    "moneda": "ARS",
    "componentes": [
      {
        "componente": 101,
        "valorAplicado": 1.5000,
        "baseCalculo": 3300.0000,
        "importeCalculado": 49.5000,
        "subtotalResultante": 3349.5000
      },
      {
        "componente": 103,
        "valorAplicado": 115.0000,
        "baseCalculo": 3349.5000,
        "importeCalculado": 115.0000,
        "subtotalResultante": 3464.5000
      },
      {
        "componente": 104,
        "valorAplicado": 3.2000,
        "baseCalculo": 3349.5000,
        "importeCalculado": 107.1840,
        "subtotalResultante": 3571.6840
      },
      {
        "componente": 105,
        "valorAplicado": 45.0000,
        "baseCalculo": 3349.5000,
        "importeCalculado": 45.0000,
        "subtotalResultante": 3616.6840
      },
      {
        "componente": 106,
        "valorAplicado": 21.0000,
        "baseCalculo": 3616.6840,
        "importeCalculado": 759.5036,
        "subtotalResultante": 4376.1876
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
      "precioVenta": 5907.85
    },
    {
      "listaPrecio": 200,
      "margenPorcentaje": 22.0000,
      "precioVenta": 5339.95
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
- separa por `etapaAplicacion` del maestro
- dentro de cada etapa ordena por `codigo` solo para persistencia y lectura consistente
- recalcula la base, el importe y el subtotal resultante
- compara contra lo recibido

### Regla importante sobre semantica

El producto no define:

- tipo de valor
- etapa de aplicacion

Eso sale del maestro `componenteCosto`.

### Costos

El backend parte siempre de:

- `costo.costoBase`

Luego:

1. toma los componentes enviados
2. consulta su definicion maestra
3. agrupa por `ETAPA_1`, `ETAPA_2` y `ETAPA_FINAL`
4. calcula todos los componentes de `ETAPA_1` sobre `costoBase`
5. obtiene `neto1`
6. calcula todos los componentes de `ETAPA_2` sobre `neto1`
7. obtiene `neto2`
8. calcula todos los componentes de `ETAPA_FINAL` sobre `neto2`
9. obtiene `costoFinal`
10. compara `neto1`, `neto2` y `costoFinal` contra lo recibido

### Formula exacta por etapa

Si el componente es `PORCENTAJE`:

```text
importeCalculado = baseEtapa * valorAplicado / 100
```

Si el componente es `FIJO`:

```text
importeCalculado = valorAplicado
```

Si el valor es negativo:

- descuenta
- no requiere un tipo especial

Resultado de etapa:

```text
resultadoEtapa = baseEtapa + suma(importes de la etapa)
```

### Etapas soportadas hoy

- `ETAPA_1`
- `ETAPA_2`
- `ETAPA_FINAL`

Implementacion actual:

- todos los componentes de una misma etapa usan la misma base de etapa
- el orden dentro de la etapa no cambia el resultado matematico
- el backend ya no deduce comportamiento por nombre de IVA para calcular bases

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
- tolerancia de comparacion para costo: `0.0001`
- tolerancia de comparacion para precio: `0.01`

## Regla importante para UI de componentes

- el frontend deja elegir que componentes usa el producto
- no debe agregar componentes automaticamente por una regla global
- si un componente maestro tiene `editableEnProducto = false`, el valor debe mostrarse bloqueado
- no se puede repetir el mismo componente dentro del mismo producto

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
- 8 componentes de costo

Sucursales actuales:

- `1` Casa Central
- `2` Sucursal Centro
- `3` Sucursal Norte

Listas de precio actuales:

- `100` Minorista
- `200` Mayorista
- `300` Online

Componentes de costo actuales:

- `101` Merma Operativa
- `102` Descuento Promocional
- `103` Flete Logistico
- `104` Ingresos Brutos
- `105` Tasa Municipal
- `106` IVA 21
- `107` IVA 10.5
- `108` Abasto

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
