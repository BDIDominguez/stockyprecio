# Modelo Comercial de Producto

## Proposito
Este documento define la estructura final a implementar para manejar el producto comercial en una primera version solida, simple y extensible.

La meta es cubrir:
- producto base
- stock por sucursal
- costo vigente
- impuestos configurables
- listas de precios
- precios vigentes almacenados sin recalculo en consultas
- cambios inmediatos o programados
- historial de cambios

Este documento reemplaza las ideas preliminares y debe tomarse como referencia de implementacion.

## Criterios Confirmados
- El costo es unico por producto.
- Un producto puede tener cero, uno o muchos impuestos.
- IVA es solo un impuesto mas dentro del modelo.
- Las listas de precios son entidades propias con nombre configurable.
- Los precios vigentes se almacenan completos para evitar recalculos al consultar.
- El precio de venta puede definirse por margen o por precio fijo.
- La fuente de verdad es siempre el valor vigente almacenado.
- La actualizacion comercial puede ser inmediata o programada.
- Toda actualizacion comercial debe quedar registrada en historial.
- Los calculos internos trabajan con 4 decimales.
- El precio final de venta se almacena con 2 decimales usando redondeo `HALF_UP`.

## Resumen del Modelo
El modelo se divide en dos grupos:

### Datos vigentes
- `Producto`
- `Stock`
- `Impuesto`
- `ProductoImpuesto`
- `ListaPrecio`
- `ProductoCosto`
- `ProductoPrecio`

### Cambios e historial
- `ProductoActualizacion`
- `ProductoActualizacionDato`
- `ProductoActualizacionCosto`
- `ProductoActualizacionImpuesto`
- `ProductoActualizacionPrecio`

## Organizacion por Dominio
Para respetar la estructura actual del proyecto, la implementacion debe separarse por dominio y no concentrarse toda dentro de `producto`.

Distribucion propuesta:

### Dominio `producto`
- `Producto`
- `CodigoDeBarra`
- `ProductoCosto`
- `ProductoPrecio`
- `ProductoImpuesto`
- `ProductoActualizacion`
- `ProductoActualizacionDato`
- `ProductoActualizacionCosto`
- `ProductoActualizacionImpuesto`
- `ProductoActualizacionPrecio`

### Dominio `impuesto`
- `Impuesto`

### Dominio `listaPrecio`
- `ListaPrecio`

Notas:
- `Impuesto` y `ListaPrecio` son catalogos propios del negocio y por eso deben tener su propio paquete.
- `ProductoImpuesto` sigue dentro de `producto` porque representa la asignacion de impuestos a un producto.
- `ProductoPrecio` sigue dentro de `producto` porque representa el precio de un producto en una lista.
- Las relaciones entre entidades se manejaran de forma manual mediante claves escalares.
- No deben agregarse anotaciones JPA de relacion como `@ManyToOne`, `@OneToMany`, `@JoinColumn`, etc.

## Entidades Vigentes

### 1. Producto
Responsabilidad:
- representar la identidad comercial del articulo

Campos:
- `id: Long`
- `codigo: Long`
- `nombre: String`
- `descripcion: String`
- `categoria: Long`
- `proveedor: Long`
- `manejaStock: Boolean`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigo`

Notas:
- `categoria` y `proveedor` siguen el criterio actual del proyecto: claves escalares, sin relaciones JPA.
- `codigo` es la identidad de negocio.

### 2. Stock
Responsabilidad:
- representar existencias por sucursal

Campos:
- `id: Long`
- `codigoProducto: Long`
- `sucursal: Long`
- `cantidad: BigDecimal`
- `reserva: BigDecimal`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigoProducto + sucursal`

Notas:
- `cantidad` puede ser negativa.
- `reserva` no puede ser negativa.

### 3. Impuesto
Responsabilidad:
- catalogo de impuestos aplicables

Campos:
- `id: Long`
- `codigo: String`
- `nombre: String`
- `descripcion: String`
- `porcentaje: BigDecimal`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigo`

Ejemplos:
- `IVA21`
- `IVA105`
- `EXENTO`
- `IMP_INTERNO`

Notas:
- permite modelar IVA, exenciones y otros impuestos futuros sin cambiar la estructura.

### 4. ProductoImpuesto
Responsabilidad:
- definir los impuestos vigentes de un producto

Campos:
- `id: Long`
- `codigoProducto: Long`
- `impuesto: Long`
- `ordenAplicacion: Integer`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigoProducto + impuesto`

Relaciones logicas:
- muchos `ProductoImpuesto` pueden pertenecer a un `Producto`
- muchos `ProductoImpuesto` pueden apuntar a un `Impuesto`

Notas:
- un producto puede no tener impuestos.
- `ordenAplicacion` permite controlar el orden si algun impuesto futuro depende del anterior.

### 5. ListaPrecio
Responsabilidad:
- definir las listas comerciales del negocio

Campos:
- `id: Long`
- `codigo: String`
- `nombre: String`
- `descripcion: String`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigo`

Ejemplos:
- `PUBLICO`
- `MAYORISTA`
- `BULTO`

### 6. ProductoCosto
Responsabilidad:
- guardar el costo vigente unico del producto

Campos:
- `id: Long`
- `codigoProducto: Long`
- `costo: BigDecimal`
- `moneda: String`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigoProducto`

Notas:
- representa el costo vigente actual.
- no se maneja costo por sucursal en esta etapa.
- `moneda` es opcional; si no se necesita ahora puede omitirse en la implementacion inicial.

### 7. CodigoDeBarra
Responsabilidad:
- guardar uno o muchos codigos de barra asociados a un producto

Campos:
- `id: Long`
- `codigoProducto: Long`
- `barra: String`

Claves:
- PK: `id`
- UK: `barra`

Relaciones logicas:
- muchos `CodigoDeBarra` pueden pertenecer a un `Producto`

Notas:
- un producto puede tener varios codigos de barra.
- `codigoProducto` no es unico.
- `barra` si debe ser unica en todo el sistema.
- no requiere `activo`, `fechaCreacion` ni `fechaModificacion` en esta primera etapa, salvo que luego aparezca una necesidad concreta de auditoria.

### 8. ProductoPrecio
Responsabilidad:
- guardar el precio vigente del producto para cada lista, listo para consultar sin recalculo

Campos:
- `id: Long`
- `codigoProducto: Long`
- `listaPrecio: Long`
- `modoCalculo: String`
- `costoBase: BigDecimal`
- `margenPorcentaje: BigDecimal`
- `precioManual: BigDecimal`
- `subtotalAntesImpuestos: BigDecimal`
- `importeImpuestos: BigDecimal`
- `precioFinal: BigDecimal`
- `activo: Boolean`
- `fechaCreacion: LocalDateTime`
- `fechaModificacion: LocalDateTime`

Claves:
- PK: `id`
- UK: `codigoProducto + listaPrecio`

Relaciones logicas:
- muchos `ProductoPrecio` pueden pertenecer a un `Producto`
- muchos `ProductoPrecio` pueden apuntar a una `ListaPrecio`

Reglas:
- `modoCalculo` admite `FIJO` o `MARGEN`.
- Si `modoCalculo = FIJO`, el valor principal de entrada es `precioManual`.
- Si `modoCalculo = MARGEN`, el valor principal de entrada es `margenPorcentaje`.
- `subtotalAntesImpuestos`, `importeImpuestos` y `precioFinal` quedan almacenados para lectura directa.

Notas:
- esta entidad guarda todo lo necesario para mostrar o vender sin recalcular.
- `costoBase` se guarda aunque exista en `ProductoCosto` para dejar persistido el valor usado al formar ese precio vigente.

## Entidades de Actualizacion e Historial

### 9. ProductoActualizacion
Responsabilidad:
- cabecera de una actualizacion comercial de producto
- sirve tanto para cambio inmediato como programado
- deja trazabilidad historica

Campos:
- `id: Long`
- `codigoProducto: Long`
- `estado: String`
- `esProgramada: Boolean`
- `fechaProgramada: LocalDateTime`
- `fechaAplicacion: LocalDateTime`
- `motivo: String`
- `observacion: String`
- `usuario: String`
- `fechaCreacion: LocalDateTime`

Claves:
- PK: `id`

Estados:
- `PENDIENTE`
- `APLICADO`
- `CANCELADO`

Notas:
- no se recomienda modelarlo con un solo `boolean`.
- el campo `estado` permite evolucionar sin rediseñar la tabla.

### 10. ProductoActualizacionDato
Responsabilidad:
- guardar los datos base del producto que una actualizacion quiere aplicar

Campos:
- `id: Long`
- `actualizacion: Long`
- `nombre: String`
- `descripcion: String`
- `categoria: Long`
- `proveedor: Long`
- `activo: Boolean`

Claves:
- PK: `id`
- UK: `actualizacion`

Notas:
- si una actualizacion no modifica datos base, este registro puede no existir.

### 11. ProductoActualizacionCosto
Responsabilidad:
- guardar el costo que tendra el producto cuando la actualizacion se aplique

Campos:
- `id: Long`
- `actualizacion: Long`
- `costo: BigDecimal`
- `moneda: String`

Claves:
- PK: `id`
- UK: `actualizacion`

### 12. ProductoActualizacionImpuesto
Responsabilidad:
- guardar los impuestos que tendra el producto cuando la actualizacion se aplique

Campos:
- `id: Long`
- `actualizacion: Long`
- `impuesto: Long`
- `ordenAplicacion: Integer`

Claves:
- PK: `id`
- UK: `actualizacion + impuesto`

Notas:
- representa la fotografia de impuestos de esa actualizacion, pero en forma normalizada.
- no usa JSON ni snapshot como estructura principal.

### 13. ProductoActualizacionPrecio
Responsabilidad:
- guardar los precios que tendra el producto por lista cuando la actualizacion se aplique

Campos:
- `id: Long`
- `actualizacion: Long`
- `listaPrecio: Long`
- `modoCalculo: String`
- `costoBase: BigDecimal`
- `margenPorcentaje: BigDecimal`
- `precioManual: BigDecimal`
- `subtotalAntesImpuestos: BigDecimal`
- `importeImpuestos: BigDecimal`
- `precioFinal: BigDecimal`

Claves:
- PK: `id`
- UK: `actualizacion + listaPrecio`

Notas:
- esta tabla permite programar o registrar cambios completos por lista.
- los valores se guardan completos para auditoria y aplicacion directa.

## Relaciones Logicas

### Vigentes
- `Producto` 1 -> N `Stock`
- `Producto` 1 -> N `CodigoDeBarra`
- `Producto` 1 -> 0..1 `ProductoCosto`
- `Producto` 1 -> N `ProductoPrecio`
- `Producto` 1 -> 0..N `ProductoImpuesto`
- `ListaPrecio` 1 -> N `ProductoPrecio`
- `Impuesto` 1 -> N `ProductoImpuesto`

### Actualizaciones
- `Producto` 1 -> N `ProductoActualizacion`
- `ProductoActualizacion` 1 -> 0..1 `ProductoActualizacionDato`
- `ProductoActualizacion` 1 -> 0..1 `ProductoActualizacionCosto`
- `ProductoActualizacion` 1 -> 0..N `ProductoActualizacionImpuesto`
- `ProductoActualizacion` 1 -> 0..N `ProductoActualizacionPrecio`

## Flujo Operativo Esperado

### Alta inicial de producto comercial
1. Se crea `Producto`
2. Si corresponde, se crean los registros de `CodigoDeBarra`
3. Se crea `ProductoCosto`
4. Se crean los registros de `ProductoImpuesto`
5. Se crean los registros de `ProductoPrecio`
6. Si corresponde, se crea `Stock` inicial por sucursal

### Modificacion inmediata
1. Se crea una `ProductoActualizacion` con estado `APLICADO`
2. Se guardan sus detalles en:
   - `ProductoActualizacionDato`
   - `ProductoActualizacionImpuesto`
   - `ProductoActualizacionPrecio`
3. Se actualizan las tablas vigentes:
   - `Producto`
   - `CodigoDeBarra`
   - `ProductoCosto`
   - `ProductoImpuesto`
   - `ProductoPrecio`

### Modificacion programada
1. Se crea una `ProductoActualizacion` con estado `PENDIENTE`
2. Se guardan sus detalles
3. Un proceso posterior aplica la actualizacion en `fechaProgramada`
4. Al aplicarse:
   - se actualizan las tablas vigentes
   - `estado` pasa a `APLICADO`
   - se completa `fechaAplicacion`

### Cancelacion
1. Una actualizacion pendiente puede pasar a `CANCELADO`
2. No debe tocar las tablas vigentes

## Regla de Calculo y Redondeo
- Todos los calculos intermedios deben trabajar con escala `4`.
- El precio final de venta debe persistirse con escala `2`.
- El redondeo final debe usar `RoundingMode.HALF_UP`.

Ejemplos:
- `0.566 -> 0.57`
- `0.564 -> 0.56`

## Decisiones de Implementacion

### Sobre impuestos
- no se usara `TipoIva` como unica solucion del dominio
- el modelo final es `Impuesto` + `ProductoImpuesto`

### Sobre precios
- no se recalcula en consultas
- `ProductoPrecio` guarda todos los importes necesarios

### Sobre historial
- no se usara JSON ni snapshot como estructura principal
- el historial queda modelado con tablas normalizadas de actualizacion

### Sobre relaciones JPA
- las relaciones entre clases seguiran el estilo actual del proyecto
- se usaran claves escalares manuales
- no se implementaran asociaciones JPA entre entidades

### Sobre programacion
- la actualizacion programada usa `estado`
- no se limita a una bandera booleana unica

## Entidades Actuales a Reemplazar
Las siguientes entidades fueron pruebas preliminares y pueden reemplazarse sin conservar el modelo actual:
- `producto.entity.ProductoCosto`
- `producto.entity.ProductoPrecio`
- `producto.entity.ProductoPrecioCalculado`
- `producto.entity.TipoIva`

## Cierre
Este documento define la estructura final objetivo para implementar el producto comercial en esta etapa del proyecto.

La prioridad del modelo es:
- claridad
- consulta rapida
- historial real
- programacion de cambios
- extensibilidad razonable sin sobredisenar
