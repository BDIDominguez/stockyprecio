# Diseno de Producto Completo

Fecha de actualizacion: 2026-03-26

## Proposito

Este documento resume el diseno vigente de `productoCompleto` como unidad operativa del sistema.

No describe el flujo viejo `crearProductoCompleto`.

## Idea central

Un producto completo no es solo la tabla `productos`.

Es la composicion de:

- producto base
- stock por sucursal
- costo vigente calculado
- detalle vigente del costo
- codigos de barra
- precios vigentes por lista

## Estructura funcional actual

### Producto base

Campos principales:

- `codigo`
- `nombre`
- `descripcion`
- `categoria`
- `proveedor`
- `manejaStock`
- `activo`

### Stock

Identidad funcional:

- `codigo + sucursal`

### Costo por etapas

Niveles vigentes:

1. `costoBase`
2. `neto1`
3. `neto2`
4. `costoFinal`

### Componentes de costo

Cada producto puede usar libremente los componentes que necesite.

Ejemplos vigentes:

- merma
- descuento
- flete
- ingresos brutos
- tasa municipal
- IVA 21
- IVA 10.5
- abasto

No existe obligacion global de IVA.

### Precios por lista

Cada producto tiene uno o muchos precios por lista.

Cada precio se calcula desde `costoFinal`.

## Regla de calculo vigente

### Etapa 1

Todos los componentes de `ETAPA_1` calculan sobre `costoBase`.

Resultado:

- `neto1`

### Etapa 2

Todos los componentes de `ETAPA_2` calculan sobre `neto1`.

Resultado:

- `neto2`

### Etapa final

Todos los componentes de `ETAPA_FINAL` calculan sobre `neto2`.

Resultado:

- `costoFinal`

## Persistencia actual

Tablas funcionales:

- `productos`
- `stocks`
- `componentes_costo`
- `producto_componentes_costo`
- `producto_costos`
- `producto_costo_detalles`
- `producto_precios`
- `listas_precios`
- `codigos_barras`

## Endpoints vigentes

- `GET /api/productos/completo`
- `GET /api/productos/completo/codigo/{codigo}`
- `POST /api/productos/completo`
- `PUT /api/productos/completo/codigo/{codigo}`

## Reglas vigentes de integridad

- no se usa `id` en API publica
- se usa `codigo`
- no puede repetirse el mismo componente dentro de un producto
- un componente en uso no puede desactivarse
- el backend valida todos los calculos recibidos
- el historial fue retirado por ahora

## Conclusion

`productoCompleto` es el flujo canonico vigente del backend para trabajar el articulo completo.
