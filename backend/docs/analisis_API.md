# Analisis de API y Refactors Pendientes

Fecha: 2026-03-26

## Alcance de este analisis

Este documento resume incoherencias, deuda tecnica y puntos de refactor detectados al revisar el backend actual.

No es un diseno nuevo. Es un registro de:

- cosas que hoy funcionan pero quedaron desalineadas
- cosas que hoy son inconsistentes entre modulos
- cosas que deberian simplificarse o estandarizarse
- riesgos funcionales o tecnicos que conviene corregir

La revision fue principalmente estatica sobre controllers, DTOs, servicios, entidades, repositorios y configuracion.

## Resumen ejecutivo

El backend hoy tiene dos niveles de madurez mezclados:

- modulos mas nuevos y bastante consistentes:
  - `categoria`
  - `proveedor`
  - `listaPrecio`
  - `componenteCosto`
  - `productoCompleto`
- modulos viejos o a medio estandarizar:
  - `producto` base
  - `movimientoTipo`
  - parte de `stock`

La mayor incoherencia actual no esta en el modelo de costos/precios sino en la convivencia entre APIs nuevas y APIs legacy.

## Prioridad alta

## 1. `movimientoTipo` quedo completamente fuera del estandar actual

Ubicacion:

- `src/main/java/com/stock/backend/movimientoTipo/controller/MovimientoTipoController.java`
- `src/main/java/com/stock/backend/movimientoTipo/service/MovimientoTipoFacadeService.java`

Problemas detectados:

- expone entidad JPA directamente, sin DTOs
- usa `PUT` para crear en lugar de `POST`
- usa rutas poco claras:
  - `/api/movimientotipo`
  - `/desactivar/{moviento}`
  - `/activar/{moviento}`
- los metodos de activar/desactivar reciben el objeto completo por body y no usan de forma coherente el path variable
- hay `System.out.println(...)` en controller
- no sigue el patron de `siguiente-codigo`, busqueda paginada, baja logica estandar y query por `codigo`

Impacto:

- modulo dificil de consumir desde frontend
- alto riesgo de incoherencia con el resto de la API
- acoplamiento innecesario entre contrato HTTP y entidad persistente

Recomendacion:

- refactor completo con el mismo patron de `categoria`
- crear DTOs de lectura y alta
- definir identificador de negocio estable
- normalizar endpoints y estados HTTP

## 2. `producto` base no esta alineado con el rol que se le asigna en la arquitectura actual

Ubicacion:

- `src/main/java/com/stock/backend/producto/controller/ProductoController.java`
- `src/main/java/com/stock/backend/producto/service/ProductoFacadeService.java`

Problemas detectados:

- el controller solo expone:
  - `GET /api/productos`
  - `GET /api/productos/codigo/{codigo}`
  - `PUT /api/productos/codigo/{codigo}`
  - `GET /api/productos/siguiente-codigo`
- no tiene activacion/desactivacion
- `GET /api/productos` devuelve lista simple, no paginada
- hoy el modulo `productoCompleto` es el flujo operativo real, pero `producto` base sigue teniendo un contrato incompleto

Impacto:

- rompe la idea de usar `producto` como modulo base de estado/identidad
- obliga al frontend a resolver casos especiales
- deja un modulo a mitad de camino entre legacy y estandar

Recomendacion:

- decidir si `producto` base sera:
  - un maestro completo estandarizado
  - o un modulo interno reducido
- si se mantiene publico, agregar:
  - activacion
  - desactivacion
  - paginacion
  - filtros coherentes

## 3. `productoCompleto` todavia no resuelve la base de calculo de forma completamente explicita

Ubicacion:

- `src/main/java/com/stock/backend/application/productoCompleto/service/ProductoCompletoService.java`

Problema detectado:

- la idea funcional correcta del sistema es que cada `ComponenteCosto` indique en que etapa se aplica
- esa incoherencia ya fue corregida en el backend vigente
- hoy `ProductoCompletoService` calcula por `ETAPA_1`, `ETAPA_2` y `ETAPA_FINAL`, sin heuristica por nombre para IVA

Importante:

- el problema no es que todos los productos deban tener IVA
- el sistema no debe forzar IVA ni ningun impuesto global obligatorio
- cada producto debe poder agregar o quitar componentes segun corresponda
- un producto puede tener IVA 21, IVA 10.5, no tener IVA, o tener componentes especificos como tasas municipales

Estado actual:

- el backend ya calcula por etapas fijas
- `modoBase`, `prioridadAplicacion` y la obligatoriedad global quedaron fuera del modelo vigente
- el producto sigue eligiendo libremente que componentes usa

Riesgo residual:

- si en el futuro aparecieran mas de tres niveles reales de calculo, habria que ampliar el modelo de etapas
5. recalcular siempre segun esa definicion, sin mirar el nombre del componente

## 4. No hay una suite de pruebas que proteja el comportamiento actual

Ubicacion:

- `src/test/java/com/stock/backend/BackendApplicationTests.java`

Problemas detectados:

- solo existe `contextLoads()`
- no hay tests de:
  - paginacion
  - errores de validacion
  - `productoCompleto`
  - costos/precios
  - stock

Impacto:

- cada refactor grande depende casi por completo de prueba manual
- alto riesgo de romper contratos sin detectarlo rapido

Recomendacion:

- agregar primero tests de integracion para:
  - `productoCompleto`
  - maestros
  - manejo de errores
- luego agregar pruebas especificas del calculador de costos/precios

## Prioridad media

## 5. Convenciones de rutas no estan unificadas entre modulos

Ejemplos:

- `categoria` usa `PUT /api/categorias/{codigo}`
- `proveedor` usa `PUT /api/proveedores/{codigo}`
- `sucursal` usa `PUT /api/sucursal/{codigo}`
- `listaPrecio` usa `PUT /api/listas-precios/codigo/{codigo}`
- `componenteCosto` usa `PUT /api/componentes-costo/codigo/{codigo}`
- `productoCompleto` usa `PUT /api/productos/completo/codigo/{codigo}`

Impacto:

- consumo frontend menos predecible
- mas ifs y casos especiales

Recomendacion:

- elegir una sola convencion para lectura y modificacion por codigo:
  - o siempre `/{codigo}`
  - o siempre `/codigo/{codigo}`

## 6. Algunos DTOs de actualizacion reutilizan DTOs de lectura con campos que no deberian viajar desde frontend

Ejemplos:

- `CategoriaController` modifica usando `CategoriaDTO`
- `ProveedorController` modifica usando `ProveedorDTO`
- `SucursalController` modifica usando `SucursalDTO`

Problema:

- los DTOs de lectura tienen campos que no son necesarios para update
- en algunos casos incluyen auditoria o timestamps

Impacto:

- contratos menos claros
- riesgo de confundir al frontend
- mezcla de responsabilidades entre lectura y escritura

Recomendacion:

- crear DTOs especificos para update donde haga falta
- o reutilizar los DTOs de alta si la semantica coincide

## 7. El modulo `producto` sigue demasiado fragmentado en servicios viejos

Ubicacion:

- `ProductoConsultarTodosActivosServices`
- `ProductoConsultarTodosInactivosServices`
- `ProductoConsultarTodosServices`
- `ProductoConsultaPorCodigoService`
- `ProductoCrearService`
- `ProductoModificarService`
- `ProductoFacadeService`

Problema:

- hay demasiados servicios chicos para operaciones simples
- el facade solo reenvia

Impacto:

- mas archivos para entender un flujo basico
- mantenimiento mas costoso

Recomendacion:

- unificar en un servicio mas simple y coherente
- reservar la fragmentacion fina para logica realmente compleja

## 8. `GlobalExceptionHandler` expone mensajes de excepcion inesperadas tal cual salen de Java

Ubicacion:

- `src/main/java/com/stock/backend/common/exception/GlobalExceptionHandler.java`

Problema:

- en `@ExceptionHandler(Exception.class)` devuelve `ex.getMessage()` al cliente

Impacto:

- puede filtrar detalles tecnicos
- mensajes poco amigables o poco estables

Recomendacion:

- para errores inesperados responder un detalle mas generico
- dejar la excepcion completa solo en logs

## 9. Configuracion de Spring esta dispersa entre `application.yml` y `application.properties`

Ubicacion:

- `src/main/resources/application.yml`
- `src/main/resources/application.properties`

Problema:

- datasource esta en YAML
- `spring.jpa.hibernate.ddl-auto=update` esta en properties

Impacto:

- mas facil pasar por alto configuraciones
- peor legibilidad operativa

Recomendacion:

- consolidar configuracion en un solo archivo por perfil

## 10. Falta un perfil de test o base aislada para pruebas automatizadas

Problema:

- el backend apunta a MySQL real del entorno
- no hay perfil de test separado

Impacto:

- hace costoso automatizar
- fomenta prueba manual

Recomendacion:

- agregar `application-test.yml`
- usar base aislada o contenedor efimero para tests

## Prioridad baja

## 11. Hay errores de naming y consistencia de idioma

Ejemplos:

- `OperacionNoValidaExeption` tiene typo en `Exeption`
- clases como `ProductoConsultarTodosActivosServices` mezclan singular/plural
- hay mezcla de ingles y espanol en nombres tecnicos

Impacto:

- menor legibilidad
- da senal de deuda acumulada

Recomendacion:

- normalizar naming gradualmente

## 12. Hay comentarios y cadenas con problemas de encoding

Ejemplos visibles:

- textos tipo `CÃ³digo`
- comentarios con acentos rotos en algunas clases legacy

Impacto:

- mala lectura
- documentacion interna menos profesional

Recomendacion:

- revisar encoding UTF-8 del proyecto
- limpiar comentarios y literales rotos

## 13. Hay scripts operativos importantes en `target/`

Ejemplos:

- `target/reseed-dev-db.ps1`
- `target/reseed-dev-db.sql`

Problema:

- `target/` suele ser efimero
- puede limpiarse en cualquier momento

Impacto:

- se pierde tooling util del entorno

Recomendacion:

- mover scripts operativos permanentes a una carpeta estable, por ejemplo `scripts/` o `ops/`

## Cosas que hoy estan razonablemente bien

- `productoCompleto` ya tiene un flujo operativo coherente y usable
- `componenteCosto` quedo mejor alineado con el dominio que el viejo `impuesto`
- `stock` tiene identidad funcional correcta por `codigo + sucursal`
- `categoria`, `proveedor`, `listaPrecio` y `sucursal` ya estan bastante cerca de un patron comun
- el manejo centralizado de errores es mejor que el estado original del proyecto

## Orden sugerido de correccion

1. refactor de `movimientoTipo`
2. decision y estandarizacion definitiva de `producto` base
3. endurecer semantica de `ComponenteCosto` para no depender del nombre "IVA"
4. agregar tests de integracion
5. unificar rutas y DTOs de update
6. limpiar naming, encoding y configuracion

## Estado del analisis

Este documento debe actualizarse cuando:

- se cierre un refactor importante
- se elimine un modulo legacy
- se detecte una nueva incoherencia estructural
