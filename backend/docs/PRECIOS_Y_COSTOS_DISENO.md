# Diseno Definitivo de Costos y Precios

Documento funcional y de base de datos para el modelo de costos y precios del proyecto.

Fecha de actualizacion: 2026-03-26.

## Objetivo

Definir:

- que datos debe guardar el sistema
- como se relacionan las tablas
- como se calcula el costo final
- como se calculan los precios por lista
- que reglas de integridad y validacion deben cumplirse

Este documento describe el modelo objetivo. No define pasos de implementacion ni metodologia de trabajo.

## Principios funcionales

### 1. Backend como fuente de verdad

El frontend puede asistir al operador con calculos en vivo, pero el backend siempre debe recalcular y validar todo.

### 2. Calculo en escritura, no en lectura

El costo y los precios se calculan al crear o modificar.

En lectura:

- no se recalcula
- solo se devuelve el resultado vigente y su detalle persistido

### 3. Semantica centralizada

La semantica del componente no puede quedar librada al producto.

El sistema debe centralizar en una definicion maestra:

- la etapa de aplicacion
- el tipo de valor
- si el valor es editable o no

### 4. Trazabilidad completa

Debe poder reconstruirse exactamente:

- con que costo base se calculo
- que componentes participaron
- sobre que base se aplico cada uno
- cuanto agrego cada componente
- cual fue el costo final
- que precios por lista quedaron vigentes

## Problema funcional que resuelve este diseno

El sistema no debe permitir errores como:

1. aplicar IVA
2. aplicar Ingresos Brutos sobre una base que ya incluye IVA

Si negocio define que:

- Ingresos Brutos se calcula sobre una base sin IVA
- IVA va al final

entonces esa regla debe quedar determinada por la definicion maestra del componente y no por un orden libre cargado en el producto.

## Modelo de datos propuesto

## 1. `productos`

La tabla `productos` sigue siendo la entidad base del articulo y mantiene su responsabilidad actual.

Campos relevantes ya existentes:

- `id`
- `codigo`
- `nombre`
- `descripcion`
- `categoria`
- `proveedor`
- `maneja_stock`
- `activo`
- `fecha_creacion`
- `fecha_modificacion`

No debe guardar:

- costo
- costo final
- precios de venta
- impuestos aplicados
- margen

## 2. `listas_precios`

La tabla `listas_precios` sigue siendo el catalogo de listas comerciales.

Campos relevantes ya existentes:

- `id`
- `codigo`
- `nombre`
- `descripcion`
- `activo`
- `fecha_creacion`
- `fecha_modificacion`

No debe guardar formulas de calculo del producto. La lista solo identifica un canal o politica comercial.

## 3. `componentes_costo`

Tabla maestra nueva o refactor fuerte del modulo actual `impuesto`.

Responsabilidad:

- definir la semantica del componente
- definir como se calcula
- definir en que etapa se aplica

### Estructura propuesta

- `id` bigint PK
- `codigo` bigint unique not null
- `nombre` varchar(100) not null
- `descripcion` varchar(500) null
- `tipo_componente` varchar(30) not null
- `tipo_valor` varchar(20) not null
- `valor_defecto` decimal(12,4) not null default 0.0000
- `etapa_aplicacion` varchar(40) not null
- `editable_en_producto` boolean not null
- `activo` boolean not null
- `fecha_creacion` datetime null
- `fecha_modificacion` datetime null

### Reglas

- `codigo` es el identificador de negocio
- `tipo_componente` distingue la naturaleza del componente
- `tipo_valor` define si es `PORCENTAJE` o `FIJO`
- `valor_defecto` es el valor base del componente y no debe ser null
- `etapa_aplicacion` define si el componente trabaja sobre:
  - `costoBase`
  - `neto1`
  - `neto2`
- `editable_en_producto` indica si el operador puede modificar el valor para un producto

### Valores sugeridos para `tipo_componente`

- `IMPUESTO`
- `GASTO`
- `RECARGO`
- `AJUSTE`

### Valores sugeridos para `etapa_aplicacion`

- `ETAPA_1`
- `ETAPA_2`
- `ETAPA_FINAL`

## 4. `producto_componentes_costo`

Tabla de configuracion vigente por producto.

Responsabilidad:

- indicar que componentes aplican al producto
- guardar el valor efectivamente usado para ese producto
- permitir overrides puntuales sin romper la semantica del catalogo

### Estructura propuesta

- `id` bigint PK
- `producto` bigint not null
- `componente` bigint not null
- `valor_aplicado` decimal(12,4) not null default 0.0000
- `activo` boolean not null
- `fecha_creacion` datetime null
- `fecha_modificacion` datetime null

### Reglas

- unique (`producto`, `componente`)
- `producto` referencia de negocio a `productos.codigo`
- `componente` referencia de negocio a `componentes_costo.codigo`
- `valor_aplicado` siempre debe persistirse
- si el operador no informa un valor especifico, el sistema copia `valor_defecto` del componente maestro al crear la configuracion del producto
- esta tabla no define:
  - orden
  - tipo de valor
  - base de calculo
  - nombre del componente

## 5. `producto_costos`

Resumen vigente del costo del producto.

### Estructura propuesta

- `id` bigint PK
- `producto` bigint unique not null
- `costo_base` decimal(12,4) not null
- `neto1` decimal(12,4) not null
- `neto2` decimal(12,4) not null
- `costo_final` decimal(12,4) not null
- `moneda` varchar(20) not null default 'ARS'
- `activo` boolean not null
- `fecha_calculo` datetime not null
- `fecha_creacion` datetime null
- `fecha_modificacion` datetime null

### Reglas

- existe un solo costo vigente por producto
- `costo_base` es el punto de partida
- `neto1` es el resultado de la primera etapa
- `neto2` es el resultado de la segunda etapa
- `costo_final` es el resultado luego de aplicar todos los componentes
- `fecha_calculo` marca el momento exacto del ultimo recalculo persistido
- la tabla no es historica; contiene solo el estado vigente del producto

## 6. `producto_costo_detalles`

Detalle persistido vigente del costo.

Responsabilidad:

- guardar la ejecucion real del calculo
- dejar trazabilidad exacta

### Estructura propuesta

- `id` bigint PK
- `producto` bigint not null
- `componente` bigint not null
- `etapa_aplicacion` varchar(40) not null
- `resultado_etapa` varchar(40) not null
- `tipo_valor_aplicado` varchar(20) not null
- `valor_aplicado` decimal(12,4) not null
- `base_calculo` decimal(12,4) not null
- `importe_calculado` decimal(12,4) not null
- `subtotal_resultante` decimal(12,4) not null
- `fecha_calculo` datetime not null

### Reglas

- unique (`producto`, `componente`)
- existe una sola fila vigente por `producto + componente`
- cuando cambia el costo del producto, este detalle se reemplaza por completo
- esta tabla no es historica; solo representa la composicion vigente
- `tipo_valor_aplicado`, `etapa_aplicacion` y `resultado_etapa` se persisten para dejar explicitado como fue calculado el estado vigente

## 7. `producto_precios`

Precio vigente por producto y lista.

### Estructura propuesta

- `id` bigint PK
- `producto` bigint not null
- `lista_precio` bigint not null
- `costo_final_referencia` decimal(12,4) not null
- `margen_porcentaje` decimal(8,4) not null
- `precio_venta` decimal(12,2) not null
- `activo` boolean not null
- `fecha_calculo` datetime not null
- `fecha_creacion` datetime null
- `fecha_modificacion` datetime null

### Reglas

- unique (`producto`, `lista_precio`)
- `costo_final_referencia` es el costo final vigente usado al momento del calculo
- `precio_venta` es el resultado comercial persistido
- si un precio queda por debajo del costo, eso se detecta en tiempo de calculo y validacion; no es obligatorio persistir una bandera para eso

## Relaciones de negocio

Relaciones por `codigo`, siguiendo la convencion del proyecto:

- `producto_componentes_costo.producto` -> `productos.codigo`
- `producto_componentes_costo.componente` -> `componentes_costo.codigo`
- `producto_costos.producto` -> `productos.codigo`
- `producto_costo_detalles.producto` -> `productos.codigo`
- `producto_costo_detalles.componente` -> `componentes_costo.codigo`
- `producto_precios.producto` -> `productos.codigo`
- `producto_precios.lista_precio` -> `listas_precios.codigo`

## Flujo funcional del sistema

## 1. Alta o modificacion

1. recibir producto, costo base, componentes y precios
2. cargar componentes vigentes del producto
3. buscar definicion maestra de cada componente
4. agrupar por `etapa_aplicacion`
5. calcular `ETAPA_1` sobre `costo_base`
6. obtener `neto1`
7. calcular `ETAPA_2` sobre `neto1`
8. obtener `neto2`
9. calcular `ETAPA_FINAL` sobre `neto2`
10. obtener `costo_final`
11. calcular cada precio por lista
12. validar coherencia de lo recibido
13. persistir costo vigente
14. persistir detalle vigente del costo
15. persistir precios vigentes

## 2. Consulta

La consulta debe devolver el estado vigente persistido:

- datos base del producto
- costo vigente
- detalle del costo vigente
- precios vigentes
- stock opcional por sucursal

No debe recalcular nada al consultar.

## Reglas de calculo

## 1. Secuencia general

1. tomar `costo_base`
2. resolver componentes activos del producto
3. completar semantica desde `componentes_costo`
4. calcular todos los componentes de `ETAPA_1` sobre `costo_base`
5. obtener `neto1`
6. calcular todos los componentes de `ETAPA_2` sobre `neto1`
7. obtener `neto2`
8. calcular todos los componentes de `ETAPA_FINAL` sobre `neto2`
9. obtener `costo_final`
10. calcular `precio_venta` por lista

## 2. Reglas de componentes

- si `tipo_valor = FIJO`, `importe_calculado = valor_aplicado`
- si `tipo_valor = PORCENTAJE`, `importe_calculado = base_calculo * valor_aplicado / 100`
- `base_calculo` se determina por `etapa_aplicacion`
- el producto no puede redefinir la semantica del componente
- el valor puede ser positivo o negativo

## 3. Reglas de precio

Formula de precio:

- `precio_venta = costo_final + (costo_final * margen_porcentaje / 100)`

El sistema guarda el resultado calculado, no una formula abstracta para recalcular en lectura.

## Politica de redondeo

- calculos intermedios: 4 decimales
- costo final: 4 decimales
- precio de venta: 2 decimales
- redondeo: `HALF_UP`
- tolerancia de comparacion para costo: `0.0001`
- tolerancia de comparacion para precio: `0.01`

## Reglas de validacion

## 1. Recalculo completo obligatorio

El backend debe recalcular siempre desde `costo_base`.

## 2. Inconsistencia de calculo

Debe rechazarse la operacion si no coincide cualquiera de estos puntos:

- `base_calculo`
- `importe_calculado`
- `subtotal_resultante`
- `neto1`
- `neto2`
- `costo_final`
- `precio_venta`

## 3. Venta bajo costo

Regla:

- si `precio_venta < costo_final` y no existe confirmacion explicita, se rechaza
- si existe confirmacion explicita, se permite guardar

Dato transitorio de request:

- `confirmar_bajo_costo`

No se persiste.

## Reglas de integridad de base de datos

- `productos.codigo` unique
- `listas_precios.codigo` unique
- `componentes_costo.codigo` unique
- `producto_componentes_costo` unique (`producto`, `componente`)
- `producto_costos.producto` unique
- `producto_costo_detalles` unique (`producto`, `componente`)
- `producto_precios` unique (`producto`, `lista_precio`)
- `precio_venta` no puede ser negativo
- `costo_base` no puede ser negativo
- `neto1` no puede ser negativo
- `neto2` no puede ser negativo
- `costo_final` no puede ser negativo

## Notas de diseno importantes

### 1. `ComponenteCosto` es el nombre funcional correcto

El proyecto hoy tiene una entidad `Impuesto`, pero el modelo objetivo necesita algo mas amplio que solo impuestos.

La definicion funcional correcta es `ComponenteCosto`.

Por lo tanto, si se mantiene una entidad existente, debe migrarse conceptualmente y de nombre hacia `ComponenteCosto`, no quedar como `Impuesto` si ya representa gastos, recargos y ajustes tambien.

### 2. La tabla vigente de detalle debe reflejar ejecucion real

Por eso el detalle del costo debe guardar:

- componente aplicado
- base usada
- valor usado
- importe calculado
- subtotal resultante

No alcanza con guardar solo que componentes tiene el producto.

## Resumen final del modelo

El sistema debe quedar organizado asi:

- `productos`: identidad del articulo
- `listas_precios`: catalogo comercial
- `componentes_costo`: catalogo maestro de semantica de costo
- `producto_componentes_costo`: configuracion de componentes activos por producto
- `producto_costos`: resumen vigente del costo
- `producto_costo_detalles`: detalle vigente del costo calculado
- `producto_precios`: precios vigentes por lista

Con este modelo:

- la semantica queda centralizada
- el producto no puede romper reglas fiscales
- el costo y el precio quedan persistidos
- el backend conserva control total del calculo
