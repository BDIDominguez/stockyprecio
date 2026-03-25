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


Respuestas a lo anterior (pendiende de analizar - posibles correcciones)

## Anexo: Definiciones y Mejoras al Diseño Original

### 1. Respuestas a las Preguntas de Diseño Pendientes

#### Producto Mínimo
| Pregunta | Respuesta |
|----------|----------|
| ¿Puede existir un producto sin categoría? | **Sí.** La categoría es organizativa, no crítica para la operación. Campo nullable. |
| ¿Puede existir un producto sin proveedor? | **Sí.** Aplica para servicios o productos de fabricación propia. Campo nullable. |
| ¿Puede existir un producto sin costo? | **No.** Todo producto comercializable debe tener un costo base registrado, aunque sea provisional. |
| ¿Puede existir un producto sin precio? | **No.** Un producto debe estar asociado al menos a una lista de precios con un valor definido. |
| ¿Puede existir un producto sin tipo de IVA? | **No.** El impacto fiscal es obligatorio para facturación. |

#### Costo
| Pregunta | Respuesta |
|----------|----------|
| ¿El costo base es único o puede variar por sucursal? | **Por sucursal.** Permite manejar diferencias logísticas o de negociación local. |
| ¿Los componentes de costo son históricos o solo vigentes? | **Vigentes.** Se manejan con programación de cambios, no con historial en la tabla de costos. El historial de transacciones ya registra el costo al momento de la operación. |
| ¿El costo final se guarda o siempre se recalcula? | **Se almacena pre-calculado** en una tabla específica, actualizándose solo cuando cambian sus componentes. No se recalcula en cada consulta. |

#### Precio
| Pregunta | Respuesta |
|----------|----------|
| ¿Cada lista de precios define un precio final directo o un margen sobre costo? | **Ambos.** La configuración de cada lista puede definir precio fijo, margen porcentual o recargo. El sistema calcula el precio final según la regla definida. |
| ¿El precio se guarda como valor final o se recalcula cada vez? | **Se almacena como valor final** en una tabla de precios actuales, actualizándose solo cuando cambian sus componentes o se aplica una programación. |
| ¿Se guardan precios con y sin IVA? | **Sí.** Se almacenan ambos valores para facilitar facturación y evitar errores de redondeo. |

#### Impuestos
| Pregunta | Respuesta |
|----------|----------|
| ¿Solo existirá IVA o luego pueden aparecer otros impuestos? | **Múltiples impuestos.** El diseño debe permitir la incorporación de otros impuestos (IIBB, impuestos internos, etc.) sin modificar la estructura base. |
| ¿El IVA impacta solo en precio de venta o también en algún cálculo de costo? | **Principalmente en precio de venta.** El costo puede incluir impuestos internos como componente separado, pero el IVA es típicamente de venta. |

#### Sucursal
| Pregunta | Respuesta |
|----------|----------|
| ¿Los precios son globales o pueden variar por sucursal? | **Ambos.** Por defecto, un precio aplica a todas las sucursales, pero puede sobrescribirse por sucursal específica cuando la estrategia comercial lo requiera. |
| ¿El costo final puede variar por sucursal? | **Sí.** Dado que el costo base puede variar por sucursal y los componentes pueden tener lógicas diferentes, el costo final refleja esas diferencias. |

---

### 2. Mejoras Incorporadas al Modelo Conceptual

#### 2.1. Separación de Precio Actual y Programación de Cambios

Se establece una separación clara entre:

| Entidad | Responsabilidad |
|---------|-----------------|
| **Precio Actual** | Tabla de lectura rápida con el precio vigente hoy. Única fuente de verdad para operaciones diarias. |
| **Programación de Cambios** | Espacio donde el operador prepara, revisa y programa futuras actualizaciones de precios y costos, sin afectar la operación actual. |

**Fundamento:** Permite que el operador cargue listas de precios cuando las recibe (ej. medio día) y decida aplicarlas inmediatamente o programarlas para un momento posterior (ej. inicio del día siguiente), manteniendo control total sobre el momento de impacto.

#### 2.2. Modelo de Aplicación de Cambios

Se define un flujo de trabajo con tres momentos:

1. **Carga:** El operador ingresa nuevos precios/costos en estado "pendiente". No afecta la operación actual.
2. **Programación (opcional):** Se asigna una fecha y hora para la aplicación automática.
3. **Aplicación:** Un proceso programado aplica los cambios a la tabla de precios/costos actuales en el momento definido, o de forma inmediata por decisión del operador.

#### 2.3. Manejo de Ofertas Temporales

Las ofertas con fecha de inicio y fin se manejan dentro del mismo sistema de programación, con la capacidad de:
- Definir fecha de inicio y fin automática
- Restaurar automáticamente el precio original al finalizar la oferta
- No requerir intervención manual para finalizar promociones

#### 2.4. Estrategia de Performance

Se adopta una estrategia híbrida que evita el recálculo en cada consulta:

| Componente | Estrategia |
|------------|------------|
| **Precios actuales** | Almacenados en tabla dedicada, consulta directa por JOIN simple |
| **Costos actuales** | Almacenados pre-calculados, actualizados solo cuando cambian componentes |
| **Cambios programados** | Almacenados en tablas separadas, no impactan consultas diarias |
| **Historial de precios en ventas** | Resguardado en detalle de movimientos, no en tablas de productos |

**Fundamento:** Las consultas de listados de productos no deben ejecutar lógica de cálculo en tiempo real. El cálculo se realiza cuando cambian los insumos (costo, margen, impuestos) y el resultado se almacena para lectura rápida.

---

### 3. Estructura de Entidades Propuesta

#### 3.1. Núcleo del Producto
| Entidad | Propósito |
|---------|----------|
| **Producto** | Identidad comercial: código, nombre, descripción, categoría, proveedor, banderas de comportamiento (maneja stock, activo) |

#### 3.2. Stock
| Entidad | Propósito |
|---------|----------|
| **Stock** | Existencia por sucursal. Identidad: producto + sucursal. Campos: cantidad actual, reservada |

#### 3.3. Costos
| Entidad | Propósito |
|---------|----------|
| **Costo Base** | Costo principal de compra. Puede variar por producto y sucursal. |
| **Componentes de Costo** | Conceptos adicionales que impactan el costo final: flete, embalaje, seguros, impuestos internos, etc. Permite definir tipo (fijo o porcentual) y orden de aplicación. |
| **Costo Calculado** | Resultado consolidado del costo total. Se almacena y actualiza solo cuando cambian sus componentes. |

#### 3.4. Precios
| Entidad | Propósito |
|---------|----------|
| **Lista de Precios** | Define una estrategia comercial: minorista, mayorista, distribuidor, promocional, etc. Cada lista puede definir si sus precios se calculan por precio fijo, margen sobre costo o recargo. |
| **Precio Actual** | Precio vigente hoy por producto, lista y opcionalmente sucursal. Tabla de lectura rápida para operaciones diarias. Almacena precio sin impuestos, impuestos aplicados y precio final. |
| **Programación de Cambios** | Registro de actualizaciones pendientes. Permite cargar nuevos precios/costos con estado "pendiente", fecha programada de aplicación (opcional), y en caso de ofertas, fecha de fin para restauración automática. |
| **Lote de Actualización** | Agrupador de múltiples cambios programados para facilitar la carga masiva y aplicación por conjuntos. |

#### 3.5. Impuestos
| Entidad | Propósito |
|---------|----------|
| **Impuesto** | Catálogo de impuestos aplicables: IVA, IIBB, impuestos internos, etc. |
| **Asignación de Impuestos** | Relación entre producto y los impuestos que le aplican, permitiendo que un producto tenga múltiples impuestos asociados. |

---

### 4. Principios de Diseño Confirmados

#### 4.1. Separación de Responsabilidades
- El producto base no almacena información de stock, costo o precio
- Cada aspecto tiene su propia entidad con responsabilidad clara
- Se evita la tabla gigante con columnas dispersas

#### 4.2. Separación de Dato Base y Dato Calculado
- El usuario carga datos de configuración: costo base, componentes, margen, impuestos
- El sistema calcula y almacena resultados: costo total, precio final
- El cálculo se ejecuta solo cuando cambian los insumos, no en cada consulta

#### 4.3. Separación de Operación y Programación
- Los precios y costos actuales están en tablas de lectura rápida
- Los cambios futuros se preparan en tablas de programación
- La aplicación de cambios es un proceso controlado, manual o automático

#### 4.4. No Historial en Tablas de Producto
- El historial de precios y costos al momento de la venta se almacena en los movimientos (detalle de ventas, compras)
- Las tablas de producto y sus relacionadas mantienen solo la configuración vigente y futura
- La auditoría de cambios en configuración se maneja con tablas de historial separadas si se requiere trazabilidad

#### 4.5. Flexibilidad sin Over-Engineering
- Se soportan múltiples listas de precios como entidad propia, no como columnas fijas
- Se permite que precios y costos varíen por sucursal cuando la estrategia comercial lo requiera
- Se evita la complejidad de vigencia con fechas en tablas de consulta frecuente cuando no es necesaria

---

### 5. Resumen de Decisiones Clave

| Decisión | Conclusión |
|----------|------------|
| ¿Modelo de precios? | Tabla de precios actuales (lectura rápida) + tablas de programación de cambios (preparación) |
| ¿Recálculo en consulta? | No. Se calcula al cambiar insumos y se almacena |
| ¿Historial en producto? | No. El histórico está en movimientos de ventas |
| ¿Múltiples listas? | Sí. Como entidad propia relacionada |
| ¿Precios por sucursal? | Opcional. Sobrescritura permitida |
| ¿Múltiples impuestos? | Sí. Diseño extensible desde el inicio |
| ¿Programación de cambios? | Sí. Con aplicación inmediata o diferida, y restauración automática para ofertas |