# Implementacion Actual de Producto Comercial

## Proposito
Este documento resume lo que se implemento en esta fase para dejar operativo el nuevo modelo comercial de producto sin esperar toda la logica avanzada de negocio.

La idea fue priorizar una base usable y coherente con el modelo de entidades ya definido.

## Alcance Implementado

### 1. Catalogos base operativos
Se implementaron los modulos:
- `impuesto`
- `listaPrecio`

Cada uno incluye:
- DTOs
- mappers
- service
- controller
- repository

Razon:
- son catalogos necesarios para poder cargar correctamente la configuracion comercial del producto
- no tiene sentido dejar `ProductoPrecio` o `ProductoImpuesto` operativos si antes no existen estos catalogos base

### 2. Flujo de producto comercial inmediato
Se implemento un flujo transaccional para:
- crear producto comercial completo
- consultar producto comercial por codigo
- modificar producto comercial completo de forma inmediata

Endpoint principal:
- `/api/productos/comercial`

Razon:
- el `ProductoController` anterior cubria solo una parte del modulo base
- el nuevo modelo comercial exige trabajar con producto + costo + impuestos + precios + codigos de barra como una sola unidad operativa
- por eso se implemento un servicio especifico `ProductoComercialService`

### 3. Historial de modificaciones inmediatas
Cuando se modifica un producto comercial:
- se registra una cabecera en `ProductoActualizacion`
- se guardan datos base en `ProductoActualizacionDato`
- se guardan impuestos en `ProductoActualizacionImpuesto`
- se guardan precios en `ProductoActualizacionPrecio`
- se guarda el costo en `ProductoActualizacionCosto`

Razon:
- al bajar la implementacion del modelo aparecio un hueco: el historial original no guardaba el costo
- se agrego `ProductoActualizacionCosto` para que la trazabilidad comercial no quede incompleta

## Decisiones de Implementacion

### Sin programacion diferida todavia
No se implemento aun:
- aplicacion programada futura
- scheduler
- proceso automatico que tome actualizaciones pendientes

Razon:
- eso ya es otra capa de comportamiento
- convenia primero dejar estable el flujo inmediato y las tablas vigentes

### Sin recalculo automatico
El servicio actual:
- valida estructura
- valida referencias
- persiste los importes recibidos

No recalcula:
- subtotal
- impuestos
- precio final

Razon:
- en esta fase se respeto la decision del modelo: la fuente de verdad es lo almacenado
- el usuario ya definio que para consultas no quiere recalculo
- por ahora conviene no inventar formulas de negocio antes de cerrar bien esa parte

### Reemplazo completo de detalles vigentes al modificar
En la modificacion comercial:
- codigos de barra se reemplazan completos
- impuestos se reemplazan completos
- precios se reemplazan completos

Razon:
- es una forma simple, segura y clara de dejar consistente el producto comercial en esta etapa
- evita una logica incremental innecesariamente compleja antes de tiempo

## Lo que queda pendiente

### 1. Programacion de cambios
Falta implementar:
- actualizaciones con estado `PENDIENTE`
- fecha programada real
- proceso de aplicacion automatica

### 2. Regla de calculo comercial
Falta definir e implementar:
- como se calcula el precio final cuando el modo es `MARGEN`
- como se componen varios impuestos
- como se valida consistencia entre `subtotalAntesImpuestos`, `importeImpuestos` y `precioFinal`

### 3. Stock dentro del flujo comercial
En esta fase no se integro:
- alta automatica de stock por sucursal dentro del flujo comercial nuevo

Razon:
- stock ya tiene su propia logica y reglas
- conviene integrarlo despues, cuando se cierre la decision exacta de alta operativa por sucursal

## Conclusion
Lo implementado en esta fase deja una base funcional para trabajar el producto comercial con:
- catalogos reales
- alta completa
- consulta completa
- modificacion completa
- historial inmediato

Es una base suficiente para continuar con la siguiente capa de negocio sin volver a rehacer la estructura.
