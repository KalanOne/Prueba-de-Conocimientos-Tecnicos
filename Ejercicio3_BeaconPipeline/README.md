# Recolección de Precios EoD de Market Data en Beacon

## Detalle de Requerimientos

Contexto dado:

* Se recibe un archivo CSV diario con precios de End-of-Day (EoD) de distintos instrumentos.
* Los datos deben ser validados, transformados y cargados en Beacon, que utiliza:

  * Python 3.12
  * Framework interno de Beacon
  * MongoDB como base de datos

Procesos principales:

1. Recoleción del archivo – Lectura del CSV diario.
2. Validación de datos – Confirmar que el archivo cumpla con el formato y los valores esperados.
3. Transformación / mapeo de datos – Convertir el CSV crudo al formato esperado por Beacon.
4. Carga de datos – Insertar datos transformados en Beacon / MongoDB.
5. Manejo de errores y logging – Registrar cada paso y los fallos.
6. Consideraciones de despliegue en la nube – Accesos, llaves y almacenamiento.
7. Automatización – Ejecución diaria programada.

---

## Suposiciones y Ambigüedades

Como los requisitos son cortos, plantearía interrogantes o asumiría lo siguiente:

* Origen y formato del archivo:

  * Supongo que el CSV llega diariamente a un bucket en la nube (S3, Azure Blob o GCP Storage).
  * El esquema esperado podría ser:

    ```plaintext
    instrument_id, date, price, currency, market
    ```
  * Tamaño de archivo (<100MB).

* Reglas de validación:

  * Fechas en formato ISO (YYYY-MM-DD).
  * Precios numéricos y no negativos.
  * No existen duplicados `(instrument_id, date)` en un mismo archivo.
  * En caso de que existan errores críticos rechazar archivo y loggear.

* Integración con Beacon:

  * La carga definitiva se realiza mediante la API interna de Beacon a MongoDB.
  * Las credenciales se obtienen mediante variables de entorno o secret manager.

* Manejo de errores:

  * Supongo que no se permite carga parcial (batch atómico).

* Ejecución:

  * Puede ser un evento en la nube al llegar el archivo o un job diario programado.

---

## Diseño de Solución

### Paso 1: Reolección del archivo

* Monitorear el cloud storage para detectar nuevo archivo.
* Cuando llegue:

  1. Moverlo a un área de procesamiento para evitar reprocesos.
  2. Leerlo con Python (pandas, polars o csv module).

### Paso 2: Validación de entrada

* Validar headers y columnas requeridas.
* Validar fechas y valores numéricos.
* Detectar duplicados y valores faltantes.
* Loggear y rechazar archivo si falla validación crítica.

### Paso 3: Transformación de datos

* Estandarizar nombres de columnas según formato Beacon.
* Convertir fechas a UTC si aplica.
* Mapear instrument_id externos a internos mediante una tabla de equivalencias.
* Convertir filas a formato JSON compatible con MongoDB.

### Paso 4: Carga a Beacon / MongoDB

* Conectar a la API de recolección de Beacon.
* Insertar en bulk los documentos transformados.
* Asegurar idempotencia: evitar cargas duplicadas.

### Paso 5: Logging y manejo de errores

* Mantener logs estructurados: inicio, fin, filas procesadas, errores.
* Diferenciar errores de validación vs errores de sistema.
* Enviar alerta (correo / Slack) si falla la recoleción.

### Paso 6: Consideraciones de nube

* Archivos en almacenamiento seguro (S3, Blob, GCS).
* Usar roles de IAM o identidades administradas en lugar de credenciales planas.
* Guardar llaves en secret manager.
* Manejar reintentos en caso de fallos transitorios.

### Paso 7: Automatización

* Programar recoleción diaria con:

  * Airflow, AWS Lambda + EventBridge.
* Mantener alertas de éxito/fallo.

---

## Ejemplo de Flujo Diario

Día N – Archivo recibido: `EoDPrices_2025-08-02.csv`

1. Llega el archivo al bucket y Lambda detecta evento.
2. Validación de esquema
3. Validación de duplicados
4. Transformación, JSON listo para MongoDB.
5. Inserción masiva, 10,000 filas cargadas.
6. Log de éxito, Archivo archivado.

---

## Resumen

El pipeline de recoleción propuesto:

1. Lee y valida archivos CSV diarios.
2. Transforma y mapea a formato Beacon.
3. Carga en MongoDB mediante API de Beacon.
4. Loggea y alerta ante fallos.
5. Es segura, idempotente y automatizable.
