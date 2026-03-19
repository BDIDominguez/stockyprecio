# Modulo Estandar

## Proposito
Este documento define la plantilla base para construir y refactorizar modulos del sistema.
`categoria` es la referencia principal para esta estandarizacion.

## Regla General
- Lo especifico de un modulo va dentro del modulo.
- Lo reutilizable o transversal va en `common`.
- Los casos de uso que coordinan varios modulos van en `application`.

## Estructura Base
```text
modulo/
  controller/
  service/
  repository/
  entity/
  dto/
  mapper/
  exception/   (solo si aplica)
```

## Checklist
### 1. Estructura del modulo
- [ ] Existe una carpeta propia del modulo
- [ ] Tiene subcarpetas: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`
- [ ] Solo tiene `exception/` si realmente hay excepciones propias del modulo
- [ ] No mezcla clases de otros modulos dentro de su carpeta

### 2. Entity
- [ ] La entidad representa solo la tabla
- [ ] Usa campos escalares para relaciones manuales (`Long categoria`, `Long proveedor`, etc.)
- [ ] Tiene timestamps si el modulo los necesita
- [ ] Tiene metodos de dominio simples como `actualizar()`, `activar()`, `desactivar()`
- [ ] No contiene logica de controller ni acceso a repository
- [ ] Los nombres de campos son consistentes con la base y con el resto del proyecto

### 3. Repository
- [ ] Extiende `JpaRepository`
- [ ] Tiene solo consultas de persistencia
- [ ] Los metodos derivados de Spring Data tienen nombres claros
- [ ] No contiene logica de negocio
- [ ] Si hay busqueda por activo, nombre o codigo, queda definida aqui

### 4. DTO
- [ ] Existe DTO de salida para lectura
- [ ] Existe DTO de entrada para alta si hace falta separar creacion de lectura
- [ ] Existe DTO de modificacion si la actualizacion necesita reglas distintas
- [ ] Los DTO tienen validaciones correctas (`@NotNull`, `@NotBlank`, `@Positive`, etc.)
- [ ] No se usan anotaciones incorrectas para el tipo de dato, por ejemplo `@Size` sobre `Long`

### 5. Mapper
- [ ] Convierte `entity -> DTO`
- [ ] Convierte `DTO -> entity`
- [ ] No contiene logica de negocio
- [ ] Si hay DTO de alta separado, tiene mapper separado o metodo separado
- [ ] El mapeo mantiene nombres y significado consistentes

### 6. Service
- [ ] Contiene la logica de negocio del modulo
- [ ] Valida existencia antes de modificar o desactivar
- [ ] Valida duplicados si corresponde
- [ ] Lanza excepciones correctas en vez de devolver `null`
- [ ] No devuelve `ResponseEntity`
- [ ] No contiene logica HTTP
- [ ] Si hay paginacion, filtro y orden, se resuelve aqui junto al repository

### 7. Controller
- [ ] Expone endpoints claros y consistentes
- [ ] Recibe DTOs, no entidades crudas si no hace falta
- [ ] Devuelve DTOs o `Page<DTO>`
- [ ] Usa codigos HTTP correctos
- [ ] `POST` de alta devuelve `201 Created`
- [ ] `GET` devuelve `200 OK`
- [ ] `DELETE` logico y `PATCH` de activacion pueden devolver `204 No Content`
- [ ] No contiene logica de negocio compleja
- [ ] Usa validaciones en `RequestBody`, `PathVariable` y `RequestParam`

### 8. Excepciones
- [ ] Usa excepciones globales en `common.exception` cuando el caso es reutilizable
- [ ] Solo crea excepciones del modulo si representan una regla muy especifica
- [ ] Toda excepcion que hereda de `BaseException` define su `HttpStatus`
- [ ] El codigo interno sigue el formato `MODULO-HTTP-XXX` o `GEN-HTTP-XXX`
- [ ] No se devuelven errores manuales inconsistentes si ya existe handler global

### 9. Respuestas HTTP
- [ ] `Not Found` devuelve `404`
- [ ] `Duplicado` devuelve `409`
- [ ] `Error de validacion` devuelve `400`
- [ ] `Error inesperado` devuelve `500`
- [ ] El modulo deja que `GlobalExceptionHandler` resuelva las excepciones
- [ ] No mezcla `ResponseEntity.notFound()` manual con excepciones si ya hay patron comun

### 10. Consistencia del modulo
- [ ] Nombres de metodos siguen una misma idea: `crear`, `modificar`, `buscarPorCodigo`, `activarPorCodigo`, `desactivarPorCodigo`
- [ ] Si existe `activo/inactivo`, se maneja igual que en `categoria`
- [ ] Si existe codigo de negocio, se consulta por codigo y no solo por id interno
- [ ] Si hay `siguienteCodigo()`, su uso queda claro como sugerencia y no como garantia absoluta
- [ ] El modulo se parece estructuralmente a `categoria` salvo diferencias reales del negocio

### 11. Casos compuestos
- [ ] Si una operacion toca varios modulos, no se fuerza dentro de un solo service de entidad
- [ ] Se crea un caso de uso en `application/`
- [ ] `application` coordina servicios de varios modulos
- [ ] Cada modulo conserva su responsabilidad

### 12. Verificacion minima
- [ ] Compila
- [ ] Los endpoints principales responden con el HTTP correcto
- [ ] Los errores pasan por `GlobalExceptionHandler`
- [ ] Las validaciones del DTO responden con formato `ApiError`
- [ ] El modulo nuevo sigue el mismo patron que `categoria`

## Resumen Operativo
- `entity` guarda datos y reglas simples del objeto
- `repository` consulta y guarda
- `service` decide la logica de negocio
- `controller` recibe la peticion y devuelve la respuesta
- `dto` define que entra y que sale por API
- `mapper` traduce entre `entity` y `dto`
- `common` guarda lo reutilizable
- `application` coordina casos que usan varios modulos
