# Customer Table Merger – Dual Implementation (JTable & OpenJVS)

> Nota Importante:  
> En el ejercicio propuesto originalmente se indica que se está trabajando con OpenJVS, pero en los requisitos de la prueba técnica se solicita trabajar con `JTable` y devolver un `DefaultTableModel`.  
> 
> Por este motivo, se desarrollaron dos versiones de la misma funcionalidad:  
> 1. Versión para `JTable` – Devuelve un `DefaultTableModel`.  
> 2. Versión para `OpenJVS Table` – Devuelve una `Table` de OpenJVS.

---

## Descripción General

El ejercicio consiste en combinar datos de dos tablas de clientes:

1. Customer Profile (10 columnas)  
2. Customer Transactions (10 columnas)

Se deben seleccionar 5 columnas de cada tabla para formar una tabla combinada de 10 columnas, que:

- Tenga nombres de columnas con prefijos que indiquen el origen (`CustomerProfile_` o `CustomerTransactions_`).
- Contenga tantas filas como el mínimo de filas entre las dos tablas.
- Copie los valores columna por columna, conservando el tipo de dato (en OpenJVS) o como `Object` (en JTable).
- Realice validaciones sobre:
  - Tablas nulas.
  - Selección de columnas (exactamente 5 por tabla).
  - Índices de columnas fuera de rango.

---

## Versión 1 – Implementación con JTable

Clase: `CustomerTableMergerJTable`  
Paquetes:  
```java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
````

Método principal:

```java
public static DefaultTableModel mergeCustomerTables(
        JTable tableA,
        JTable tableB,
        int[] columnsA,
        int[] columnsB)
```

* Parámetros:

  * `tableA`: Primer `JTable` (Customer Profile)
  * `tableB`: Segundo `JTable` (Customer Transactions)
  * `columnsA`: Índices de columnas a usar de `tableA` (longitud 5)
  * `columnsB`: Índices de columnas a usar de `tableB` (longitud 5)
* Retorna: `DefaultTableModel` con 10 columnas.
* Características:

  * Valida parámetros y rangos.
  * Crea encabezados combinados con prefijos.
  * Combina filas hasta el mínimo número de filas disponible.
  * Separa la lógica en métodos privados:

    * `validateParameters`
    * `createCombinedHeaders`
    * `combineRows`

Ejemplo de uso:

```java
int[] columnsA = {0, 1, 2, 3, 4};
int[] columnsB = {0, 1, 2, 3, 4};

DefaultTableModel mergedModel = CustomerTableMergerJTable.mergeCustomerTables(
        tableA, tableB, columnsA, columnsB);
```

---

## Versión 2 – Implementación con OpenJVS

Clase: `CustomerTableMergerOpenJVS`
Paquetes:

```java
import com.olf.openjvs.*;
import com.olf.openjvs.enums.COL_TYPE_ENUM;
```

Método principal:

```java
public static Table mergeCustomerTables(
        Table tableA,
        Table tableB,
        int[] columnsA,
        int[] columnsB) throws OException
```

* Parámetros:

  * `tableA`: Primer `Table` (Customer Profile)
  * `tableB`: Segundo `Table` (Customer Transactions)
  * `columnsA`: Índices de columnas de `tableA` (longitud 5, 1-based)
  * `columnsB`: Índices de columnas de `tableB` (longitud 5, 1-based)
* Retorna: `Table` combinada con 10 columnas.
* Características:

  * Valida parámetros y rangos.
  * Crea columnas combinadas en la tabla resultado.
  * Combina filas hasta el mínimo número de filas disponible.
  * Copia celdas conservando el tipo: `int`, `double`, `string` y `date/time`.
  * Separa la lógica en métodos privados:

    * `validateParameters`
    * `createCombinedColumns`
    * `combineRows`
    * `copyCell`

Ejemplo de uso en OpenJVS:

```java
int[] columnsA = {1, 2, 3, 4, 5};
int[] columnsB = {1, 2, 3, 4, 5};

Table merged = CustomerTableMergerOpenJVS.mergeCustomerTables(
        tableA, tableB, columnsA, columnsB);

merged.viewTable(); // Solo para debug en OpenJVS
```

---

## Estructura de la Tabla Combinada (Modelo)

| CustomerProfile\_\* | ... | CustomerTransactions\_\* | ... |
| ------------------- | --- | ------------------------ | --- |
| Columna 1 (A)       | ... | Columna 1 (B)            | ... |
| Columna 2 (A)       | ... | Columna 2 (B)            | ... |
| ...                 | ... | ...                      | ... |
| Columna 5 (A)       | ... | Columna 5 (B)            | ... |

* Las primeras 5 columnas provienen de `Customer Profile`.
* Las últimas 5 columnas provienen de `Customer Transactions`.
* El número de filas = `min(tableA.rows, tableB.rows)`.

---

## Validaciones Implementadas

1. No nulos: Ambas tablas y arrays de columnas deben existir.
2. Exactitud de columnas: Cada array debe tener exactamente 5 columnas.
3. Rango de índices:

   * JTable → `0-based`
   * OpenJVS Table → `1-based`
4. Número de filas: Solo se combinan hasta `min(filasA, filasB)`.

Con estas dos versiones, se cubren ambos escenarios solicitados:

* Uso estándar en Swing (JTable)
* Integración en entorno OpenJVS