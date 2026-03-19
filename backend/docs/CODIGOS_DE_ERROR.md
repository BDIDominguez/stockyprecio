# Codigos de Error

## Proposito
Este documento define la estructura recomendada para los codigos de error internos de la aplicacion.

## Formato
```text
MODULO-HTTP-XXX
```

## Componentes
- `MODULO`: identificador corto del modulo. Ejemplos: `CAT`, `PROD`, `PROV`, `STK`, `GEN`
- `HTTP`: codigo de estado HTTP asociado al error. Ejemplos: `400`, `404`, `409`, `422`, `500`
- `XXX`: correlativo del error dentro del modulo. Ejemplos: `001`, `002`, `003`

## Regla Practica
- El estado HTTP define el tipo de error general
- El codigo interno define la causa exacta
- El codigo interno debe coincidir con el `HttpStatus` real que devuelve la API

## Ejemplos de Categoria
- `CAT-404-001` -> categoria no encontrada
- `CAT-409-001` -> codigo de categoria duplicado
- `CAT-409-002` -> nombre de categoria duplicado
- `CAT-400-001` -> operacion invalida en categoria

## Errores Genericos
- `GEN-400-001` -> validacion fallida del request o DTO
- `GEN-400-002` -> JSON mal formado
- `GEN-400-003` -> operacion no permitida o invalida
- `GEN-400-004` -> parametro con tipo o formato incorrecto
- `GEN-500-001` -> error interno inesperado

## Uso Recomendado
- Usar `GEN-*` solo para errores realmente globales o transversales
- Usar codigos de modulo cuando el error pertenece claramente a una entidad o caso de negocio
- Evitar codigos antiguos tipo `ERROR-005` o `ERROR-006`; deben migrarse gradualmente al formato estandar
