# Implementacion Actual de Producto

Fecha de actualizacion: 2026-03-26

## Alcance real implementado hoy

El backend ya no trabaja con `producto comercial` ni con el flujo viejo de impuestos separados.

La implementacion vigente hoy es:

- modulo base `producto`
- modulo `stock`
- modulo `componenteCosto`
- modulo `listaPrecio`
- flujo principal `productoCompleto`

## Endpoint principal vigente

El flujo operativo principal del articulo es:

- `GET /api/productos/completo`
- `GET /api/productos/completo/codigo/{codigo}`
- `POST /api/productos/completo`
- `PUT /api/productos/completo/codigo/{codigo}`

## Que resuelve hoy

### Producto base

- alta y modificacion de datos base del articulo
- consulta por codigo
- listado operativo desde `productoCompleto`

### Stock

- consulta puntual por `codigo + sucursal`
- creacion automatica de stock para todas las sucursales activas al crear un producto completo
- modificacion de reserva por sucursal desde `productoCompleto`

### Costo

El costo vigente se calcula por etapas:

1. `ETAPA_1` sobre `costoBase`
2. `ETAPA_2` sobre `neto1`
3. `ETAPA_FINAL` sobre `neto2`

Resultado persistido:

- `costoBase`
- `neto1`
- `neto2`
- `costoFinal`

### Detalle de costo

Se persiste el detalle vigente por componente con:

- `etapaAplicacion`
- `resultadoEtapa`
- `tipoValorAplicado`
- `valorAplicado`
- `baseCalculo`
- `importeCalculado`
- `subtotalResultante`

### Precios

Se persisten por lista de precio con:

- `costoFinalReferencia`
- `margenPorcentaje`
- `precioVenta`

## Reglas vigentes

- el backend recalcula siempre
- costo con 4 decimales
- precio de venta con 2 decimales
- venta bajo costo requiere confirmacion explicita
- no puede repetirse un componente en el mismo producto
- un componente en uso no puede desactivarse
- no existe historial por ahora

## Lo que ya no aplica

Ya no forman parte del estado actual:

- `ProductoComercialService`
- `ProductoImpuesto`
- `ProductoActualizacion*`
- `Impuesto` como modulo vigente
- programacion diferida de cambios

## Pendiente real

Lo pendiente importante hoy es:

- tests automaticos reales
- limpieza final de artefactos legacy
- refactor futuro de `movimientoTipo`
