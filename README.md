
# Pruebas Técnicas – Ejercicios Resueltos

Este repositorio contiene cuatro ejercicios técnicos que abarcan manipulación de datos, Java, OpenJVS, y diseño de pipelines para ingestión de market data.
Cada ejercicio incluye su código y documentación correspondiente.

---

## Ejercicio 1 – Tabla de Producto (Inventario)

Objetivo:
Implementar un gestor de inventario de productos que permita trabajar con listas de productos,
realizar operaciones básicas y administrar los datos en una tabla de productos.

Puntos clave:

* Implementación en Java.
* Uso de clases de productos y estructuras de datos para manejo del inventario.
* Incluye operaciones básicas sobre la lista de productos.

---

## Ejercicio 2 – Combinación de Tablas de Clientes

Objetivo:
Combinar dos tablas de clientes (Perfil y Transacciones) tomando 5 columnas de cada una
para formar una tabla unificada con 10 columnas.

Versiones Implementadas:

1. OpenJVS:

   * Uso de la API `Table` de OpenJVS.
   * Soporte para tipos de columna (int, double, string, datetime).
2. Java Swing / JTable:

   * Uso de `JTable` y `DefaultTableModel`.
   * Generación de tabla combinada de manera nativa en Java.

Características:

* Validación de parámetros (columnas, rangos).
* Combinación fila por fila hasta el tamaño mínimo de las tablas.
* Prefijos en headers para diferenciar origen de columnas.

---

## Ejercicio 3 – Pipeline de Ingestión de Precios EoD en Beacon

Contexto:

* Recepción diaria de un CSV con precios de mercado End-of-Day (EoD).
* Transformación y carga a Beacon, un sistema ETRM basado en Python 3.12 + MongoDB.

Diseño de Solución:

1. Recolección del archivo desde almacenamiento en la nube.
2. Validación de esquema, duplicados y tipos de datos.
3. Transformación a formato Beacon / MongoDB.
4. Mapeo de instrument\_id externos a internos mediante un lookup.
5. Carga atómica en MongoDB vía API de Beacon.
6. Logging y manejo de errores estructurados.
7. Automatización y consideraciones cloud (roles, secretos, archivado de CSV).

Resultado:
Pipeline seguro, idempotente y auditable para ingestión de market data.

---

## Ejercicio 4 – Mínimo de Operaciones para Eliminar Duplicados

Problema:
Dado un arreglo `nums`, se deben realizar operaciones que eliminen duplicados:

* Operación: Eliminar los primeros 3 elementos del arreglo.
* Si quedan menos de 3 elementos, eliminarlos todos.
* Un arreglo vacío o sin duplicados se considera válido.

Solución en Java:

* Implementación en la clase `MinimumOperationsDistinct`.
* Uso de `HashSet` para detectar duplicados.
* Ejemplos incluidos en `main`.

Complejidad:

* Tiempo: O(n²) en peor caso.
* Espacio: O(n) por el uso de `HashSet`.

---

## Estructura del Repositorio

```plaintext
├── Ejercicio1_Inventario/
│   ├── InventoryManager.java
│   ├── Product.java
│   ├── ProductColumns.java
│   └── README.md
├── Ejercicio2_CombinarTablas/
│   ├── CustomerTableMergerJTable.java
│   ├── CustomerTableMergerOpenJVS.java
│   └── README.md
├── Ejercicio3_BeaconPipeline/
│   └── README.md (diseño de solución y consideraciones)
├── Ejercicio4_MinOperationsDistinct/
│   ├── MinimumOperationsDistinct.java
│   └── README.md
└── README.md
```

---

## Resumen

* Ejercicio 1: Manejo de inventario en Java.
* Ejercicio 2: Combinación de tablas en OpenJVS y JTable.
* Ejercicio 3: Pipeline de ingestión de market data en Beacon.
* Ejercicio 4: Algoritmo para eliminar duplicados por bloques de 3 en Java.

Cada ejercicio incluye su propio README con explicación detallada,
pseudocódigo, ejemplos paso a paso y consideraciones de complejidad.