# Minimum Number of Operations to Make Array Elements Distinct

Este programa en Java resuelve el problema de calcular el número mínimo de operaciones necesarias para que todos los elementos de un arreglo sean distintos, bajo la regla:

- Operación permitida: Remover los primeros 3 elementos del arreglo.
- Si quedan menos de 3 elementos: Removerlos todos.
- Un arreglo vacío se considera que tiene elementos distintos.

---

## Descripción del Algoritmo

### Estrategia

1. Iterar en bloques de 3 elementos desde el inicio del arreglo.
2. En cada paso:

   * Verificar si los elementos restantes tienen duplicados.
   * Si no hay duplicados, detener el proceso.
   * Si hay duplicados, contar una operación y avanzar 3 posiciones.
3. Repetir hasta que no existan duplicados.

---

## Pseudocódigo

```plaintext
funcion minOperations(nums):
    si nums es null o tiene tamaño <= 1:
        retornar 0

    operaciones = 0
    inicio = 0
    n = longitud(nums)

    mientras inicio < n:
        conjunto = vacío
        duplicado = falso

        para i desde inicio hasta n-1:
            si nums[i] ya está en conjunto:
                duplicado = verdadero
                salir del bucle
            sino:
                agregar nums[i] al conjunto

        si duplicado es falso:
            salir del bucle

        operaciones++
        inicio += 3

    retornar operaciones
```

---

## Ejemplo paso a paso

### Ejemplo 1

```plaintext
nums = [1, 2, 3, 4, 2, 3, 3, 5, 7]
```

1️⃣ Inicio:
`start = 0`
Verificamos duplicados desde el inicio → Sí hay (`2` y `3` repetidos).
Operaciones = 1 → Removemos los primeros 3:

```plaintext
[4, 2, 3, 3, 5, 7]
```

2️⃣ Siguiente iteración (`start = 3` original, ahora inicio de nuevo array)
Verificamos duplicados → Sí hay (`3` repetido).
Operaciones = 2 → Removemos los primeros 3:

```plaintext
[3, 5, 7]
```

3️⃣ Última iteración
Verificamos duplicados → No hay.
Resultado: `2 operaciones`.

---

### Ejemplo 2

```plaintext
nums = [4, 5, 6, 4, 4]
```

1️⃣ Remover primeros 3 → `[4, 4]`
Duplicado → Sí.
Operaciones = 1

2️⃣ Remover los 2 restantes → `[]`
Arreglo vacío, se considera válido.
Resultado: `2 operaciones`.

---

### Ejemplo 3

```plaintext
nums = [6, 7, 8, 9]
```

1️⃣ Comprobación inicial → No hay duplicados.
Resultado: `0 operaciones`.

---

## Complejidad del Algoritmo

* Tiempo (Time Complexity):

  * En el peor caso, recorremos el arreglo completo en cada iteración mientras avanzamos de 3 en 3.
  * Aproximadamente $O(n^2 / 3)$ en el peor caso simplificado como O(n²).

* Espacio (Space Complexity):

  * Usamos un `HashSet` para detectar duplicados.
  * O(n) en el peor caso (cuando todos los elementos son distintos).

---

## Nota Importante

¿Por qué puede marcar errores al ejecutar el archivo?

* Dentro de este proyecto existen otros archivos que dependen de librerías externas (por ejemplo, OpenJVS) y fallará en esas clases que no encuentran sus dependencias.

Por eso:

* Si en algún momento necesitas compilar únicamente esta clase:

```bash
javac MinimumOperationsDistinct.java
java MinimumOperationsDistinct
```