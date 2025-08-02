# InventoryManager - Gestión de Inventario de Productos con OpenJVS

## Descripción

Este proyecto implementa un sistema básico de gestión de inventario para un almacén pequeño usando OpenJVS.  
Se utiliza la clase `Table` de OpenJVS para manejar internamente la información de productos, sus cantidades y precios.

El objetivo principal es mostrar un inventario de productos en una tabla, permitiendo:  
- Visualizar productos y sus datos  
- Actualizar la cantidad en stock tras una reposición  
- Ordenar los productos por categoría o cantidad  
- Añadir productos nuevos validando los datos

---

## Funcionalidades principales

- Inicialización: Se crea una tabla con columnas para Product ID, Nombre, Categoría, Cantidad en Stock y Precio Unitario.  
- Carga de datos: Se llena la tabla con una lista de objetos `Product` predefinidos.  
- Visualización: Se muestra la tabla con los datos cargados y ordenados.  
- Actualización: Se pueden actualizar cantidades por Product ID o por fila en la tabla.  
- Ordenación: La tabla puede ordenarse según la columna deseada (ejemplo: cantidad).  
- Validación: Se controla que no se agreguen productos con nombre vacío o cantidades/precios negativos, evitando excepciones y mostrando mensajes en consola.  
- Manejo de excepciones: Se usan bloques `try-catch` específicos para capturar errores en puntos críticos (inicialización, actualización) sin envolver todo el flujo en un bloque general.

---

## Estructura principal

- Método `execute`:  
  Punto de entrada que inicializa la tabla, carga productos, ordena, muestra la tabla y realiza algunas actualizaciones con control de errores puntuales.  

- `initProductTable`: Define las columnas y estructura de la tabla.  

- `fillTableFromList`: Llena la tabla con los datos de una lista de productos.  

- `addProduct`: Añade un producto validando que los datos sean correctos.  

- `updateQuantityByProductId` y `updateQuantityByRow`: Actualizan la cantidad de un producto, con validación y mensajes de error.  

- `sortByColumn`: Ordena la tabla por la columna especificada.

---

## Manejo de errores

- Productos con nombre vacío, cantidad o precio negativos no se agregan y generan un mensaje en consola.  
- Actualizaciones con cantidades negativas o filas inválidas lanzan excepciones capturadas para evitar que la aplicación termine abruptamente.  