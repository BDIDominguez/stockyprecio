# Limpieza Backend Pendiente

Fecha: 2026-03-26

## Resuelto hoy

### Paquetes vacios eliminados

- `src/main/java/com/stock/backend/impuesto/`
- `src/main/java/com/stock/backend/application/crearProductoCompleto/`

### Archivos raiz eliminados

- `HELP.md`
- `Estado`
- `resume.txt`

### Configuracion unificada

- se elimino `src/main/resources/application.yml`
- la configuracion quedo en `src/main/resources/application.properties`

### Documentacion vieja reescrita

- `docs/PRODUCTO_MODELO_COMERCIAL.md`
- `docs/PRODUCTO_IMPLEMENTACION_FASE_ACTUAL.md`
- `docs/PRODUCTO_COMPLETO_DISENO.md`

## Pendiente real

### 1. Modulo `movimientoTipo`

Sigue pendiente porque todavia se piensa trabajar mas adelante.

Problemas visibles:

- controller devuelve entidad directa
- endpoints viejos y poco REST
- usa `PUT` para crear y para activar/desactivar
- recibe body donde no corresponde
- naming inconsistente
- typo en path variable `moviento`
- no sigue el estandar del resto del proyecto

### 2. `ProductoController`

Todavia queda mas debil que `productoCompleto`.

Puntos a revisar:

- listado no paginado
- rol exacto del modulo base frente al flujo completo

### 3. Tests automaticos

Sigue pendiente a proposito.

Estado:

- solo existe `contextLoads()`
- no protege el comportamiento real del backend

Siguiente paso recomendado:

- agregar tests reales sobre `productoCompleto`, `componenteCosto`, errores y validaciones
