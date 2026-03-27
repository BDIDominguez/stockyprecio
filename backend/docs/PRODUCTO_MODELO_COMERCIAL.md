# Modelo Comercial de Producto

Fecha de actualizacion: 2026-03-26

## Estado actual

Este documento describe el modelo comercial vigente del backend.

Ya no aplica el modelo viejo basado en:

- `Impuesto`
- `ProductoImpuesto`
- `ProductoActualizacion*`
- `ProductoComercialService`

## Estructura comercial vigente

El modelo actual se apoya en:

- `producto` como identidad base del articulo
- `stock` por `codigo + sucursal`
- `componenteCosto` como catalogo maestro
- `productoCompleto` como flujo operativo principal
- `listaPrecio` como catalogo comercial
- `producto_costos`, `producto_costo_detalles` y `producto_precios` como estado vigente

## Regla central

El costo comercial se calcula por etapas fijas:

1. `costoBase`
2. `neto1`
3. `neto2`
4. `costoFinal`

Cada `ComponenteCosto` pertenece a una de estas etapas:

- `ETAPA_1`
- `ETAPA_2`
- `ETAPA_FINAL`

Todos los componentes de una misma etapa usan la misma base de etapa.

## Piezas funcionales actuales

### Producto

- identidad del articulo
- datos base
- estado operativo

### ComponenteCosto

- define semantica del componente
- define `tipoValor`
- define `etapaAplicacion`
- define si es editable por producto

### ProductoComponenteCosto

- define que componentes usa el producto
- guarda `valorAplicado`

### ProductoCosto

- guarda `costoBase`
- guarda `neto1`
- guarda `neto2`
- guarda `costoFinal`

### ProductoCostoDetalle

- deja trazabilidad vigente por componente
- guarda:
  - `etapaAplicacion`
  - `resultadoEtapa`
  - `tipoValorAplicado`
  - `valorAplicado`
  - `baseCalculo`
  - `importeCalculado`
  - `subtotalResultante`

### ProductoPrecio

- guarda precios vigentes por lista
- guarda:
  - `costoFinalReferencia`
  - `margenPorcentaje`
  - `precioVenta`

### ListaPrecio

- identifica el canal comercial

## Flujo principal vigente

El flujo comercial vigente es `productoCompleto`:

- `GET /api/productos/completo`
- `GET /api/productos/completo/codigo/{codigo}`
- `POST /api/productos/completo`
- `PUT /api/productos/completo/codigo/{codigo}`

## Reglas comerciales vigentes

- el backend recalcula siempre
- el costo usa 4 decimales
- el precio usa 2 decimales
- los componentes pueden tener valores positivos o negativos
- no existe obligatoriedad global de IVA
- cada producto elige sus componentes
- un componente en uso no puede desactivarse
- no hay historial activo por ahora

## Conclusion

El modelo comercial vigente del proyecto es:

- `ComponenteCosto + ProductoComponenteCosto + ProductoCosto + ProductoCostoDetalle + ProductoPrecio`

Y su flujo operativo canonico es `productoCompleto`.
