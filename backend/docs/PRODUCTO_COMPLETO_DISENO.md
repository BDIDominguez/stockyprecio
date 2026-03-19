# Diseno de Producto Completo

## Proposito
Este documento deja planteado el problema de negocio y el marco de diseno para la futura fase 2 de `producto` y `application/crearProductoCompleto`.

La idea es definir un modelo serio para:
- producto base
- stock por sucursal
- costo compuesto
- impuestos
- precio de venta
- multiples listas de precios

## Contexto del Problema
En el sistema anterior existia una sola tabla grande con toda la informacion del producto.
Eso permitia resolver el problema rapido, pero terminaba siendo dificil de mantener, extender y razonar.

En este proyecto se busca un modelo mas claro y escalable, parecido al enfoque de un ERP mas serio:
- separar responsabilidades
- evitar tablas gigantes con demasiadas columnas
- permitir recalculo y trazabilidad
- soportar distintas listas de precios
- mantener flexibilidad para futuros cambios

## Vision de Negocio
Un producto completo no deberia ser solo un registro en la tabla `productos`.
Deberia representar una composicion de informacion comercial y operativa.

Un producto completo deberia contemplar al menos:
- datos base del producto
- stock por sucursal
- costo base de compra
- componentes adicionales del costo
- tipo de IVA u otro esquema impositivo
- costo total calculado
- una o varias listas de precios de venta

## Componentes Esperados
### 1. Producto base
Responsabilidad:
- identidad comercial del articulo
- codigo
- nombre
- descripcion
- categoria
- proveedor
- comportamiento general como `manejaStock`, `activo`, `tipoIva`

Tabla relacionada:
- `productos`

### 2. Stock por sucursal
Responsabilidad:
- existencia por `codigo + sucursal`
- cantidad actual
- reserva

Tabla relacionada:
- `stocks`

Observacion:
- ya se definio que la identidad de negocio del stock es `codigo + sucursal`

### 3. Costo base
Responsabilidad:
- registrar el costo principal de compra del producto
- puede variar por sucursal, proveedor o contexto futuro

Posible camino:
- separar `costo base` de `componentes adicionales`

### 4. Componentes de costo
Responsabilidad:
- sumar conceptos que impactan en el costo final
- ejemplos: flete, embalaje, merma, impuestos internos, seguros, gastos administrativos

La entidad `ProductoCosto` ya apunta en esta direccion.

### 5. Impuestos
Responsabilidad:
- definir la estructura tributaria aplicada a la venta
- IVA u otros impuestos que afecten el precio final

La entidad `TipoIva` ya es un primer paso.

### 6. Costo total calculado
Responsabilidad:
- consolidar costo base + componentes + impacto impositivo que forme parte del costo
- dejar trazabilidad del resultado

La entidad `ProductoPrecioCalculado` puede evolucionar o separarse mejor segun el calculo final deseado.

### 7. Listas de precios
Responsabilidad:
- permitir distintas estrategias de venta
- ejemplos: lista minorista, mayorista, distribuidor, promo, web, sucursal especial

Esto no deberia resolverse con una sola columna de precio.
Deberia modelarse como una estructura propia.

## Propuesta Conceptual Inicial
Una opcion solida para este proyecto seria pensar el dominio asi:

### Producto
- representa el articulo en si

### Stock
- representa existencia por sucursal

### ProductoCostoBase
- costo principal de compra

### ProductoCostoComponente
- componentes adicionales del costo

### ProductoCostoCalculado
- resultado consolidado del costo final

### ListaPrecio
- define una lista comercial
- por ejemplo: minorista, mayorista, especial

### ProductoListaPrecio
- precio final del producto para una lista especifica
- puede guardar margen, precio sin IVA, IVA, precio final con IVA

### TipoIva
- configuracion tributaria del producto

## Enfoque recomendado
### Evitar tabla gigante
No volver al modelo de una tabla unica con todos los datos.
Eso se vuelve inmanejable cuando crecen:
- listas de precios
- sucursales
- costos adicionales
- historicos
- reglas impositivas

### Separar dato base de dato calculado
Conviene separar:
- lo que el usuario carga manualmente
- lo que el sistema calcula

Ejemplo:
- el usuario carga costo base, margen, tipo de IVA y componentes
- el sistema calcula costo total y precio final

### Permitir recalculo
Un ERP serio normalmente no depende de guardar todo mezclado.
Suele guardar:
- configuracion base
- relaciones
- resultados calculados

Y luego recalcula cuando cambia:
- un costo
- un impuesto
- una lista
- un margen

### Soportar multiples listas de precios como entidad propia
Lo mas sano es que las listas de precios no sean columnas fijas.
Ejemplo no recomendado:
- `precioLista1`
- `precioLista2`
- `precioLista3`

Ejemplo recomendado:
- tabla `listas_precios`
- tabla `producto_lista_precio`

Eso permite:
- agregar listas sin tocar estructura
- activar/desactivar listas
- recalcular una o varias
- asociar reglas por lista

## Como suelen pensarlo sistemas mas serios
En sistemas grandes o ERP de primera linea, normalmente el producto no vive como un solo bloque gigante.
Lo habitual es separar:
- maestro de articulos
- existencias
- costos
- impuestos
- precios por lista o canal
- reglas de calculo

La filosofia general suele ser:
- una entidad base chica y estable
- tablas relacionadas para aspectos variables
- procesos de calculo para formar resultados comerciales

Eso permite:
- auditar mejor
- recalcular mejor
- evitar columnas inutiles o repetidas
- soportar crecimiento del negocio

## Preguntas de diseno pendientes
Estas preguntas deben resolverse antes de implementar la fase 2:

### Producto minimo
- puede existir un producto sin categoria?
- puede existir un producto sin proveedor?
- puede existir un producto sin costo?
- puede existir un producto sin precio?
- puede existir un producto sin tipo de IVA?

### Costo
- el costo base es unico o puede variar por sucursal?
- los componentes de costo son historicos o solo vigentes?
- el costo final se guarda o siempre se recalcula?

### Precio
- cada lista de precios define un precio final directo o un margen sobre costo?
- el precio se guarda como valor final o se recalcula cada vez?
- se guardan precios con y sin IVA?

### Impuestos
- solo existira IVA o luego pueden aparecer otros impuestos?
- el IVA impacta solo en precio de venta o tambien en algun calculo de costo?

### Sucursal
- los precios son globales o pueden variar por sucursal?
- el costo final puede variar por sucursal?

## Siguiente paso recomendado
Antes de programar la fase 2, conviene definir un modelo de dominio simple y estable para:
- producto base
- costo base
- componentes de costo
- listas de precios
- precio calculado por lista

Luego de eso, se puede disenar correctamente:
- el DTO de alta completa
- el flujo transaccional de `crearProductoCompleto`
- las tablas faltantes
- las reglas de recalculo

## Conclusion
La meta no deberia ser solo "crear producto".
La meta real es construir un modelo comercial donde el producto completo sea una composicion ordenada de:
- identidad
- stock
- costo
- impuestos
- precio
- listas de precios

Eso es mucho mas cercano a como trabajan sistemas grandes, y evita volver a la tabla gigante inmanejable del sistema anterior.
