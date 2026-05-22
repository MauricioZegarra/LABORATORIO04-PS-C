# 📦 Gestor de Inventario - Ejercicio 1 (Pruebas de Software)

Este repositorio contiene la resolución del **Ejercicio Propuesto 1: Gestor de Inventario**, enfocado en la aplicación rigurosa de metodologías de **Pruebas Unitarias** empleando el framework **JUnit 5**.

El sistema permite gestionar el inventario físico de productos, controlando estrictamente las entradas, salidas y registrando un historial inmutable (kárdex) de todos los movimientos transaccionales.

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje:** Java (Compatible con Java 11+)
* **Gestor de Dependencias:** Apache Maven
* **Framework de Pruebas:** JUnit 5 (Jupiter API & Params)
* **Interfaz Gráfica:** Java Swing (Incluida para propósitos visuales)

---

## 🏗️ Estructura del Proyecto

El proyecto sigue la estructura estándar de un proyecto Maven:

```text
gestor-inventario/
├── src/
│   ├── main/java/pe/com/lab04/       # Lógica del Negocio (Core)
│   │   ├── Producto.java             # Entidad principal (100% testeada)
│   │   ├── Movimiento.java           # Registro histórico (100% testeado)
│   │   ├── InventarioException.java  # Excepciones personalizadas
│   │   └── ... (GUI, CSV y App principal)
│   └── test/java/pe/com/lab04/       # Suite de Pruebas Unitarias
│       └── ProductoTest.java         # 34 Casos de prueba exhaustivos
└── pom.xml                           # Configuración de dependencias (JUnit 5)
```

---

## 🧪 Estrategia de Pruebas Unitarias (QA)

El enfoque principal de este proyecto no es la Interfaz Gráfica, sino la **calidad, robustez y resiliencia del Código de Negocio**. Se ha diseñado una suite de pruebas que asegura un **100% de Line & Branch Coverage** sobre las clases de dominio lógico (`Producto` y `Movimiento`).

### Cumplimiento de Requisitos (Rúbrica)
1. **Volumen de Pruebas:** Se implementaron **34 escenarios únicos** (superando el mínimo de 12).
2. **Cobertura Total (100%):** Se validó cada bloque `if`, excepción, método de clase y casos `toString`.
3. **Casos Excepcionales:** Múltiples tests inyectando valores `null`, strings vacíos, números negativos e intentos de sobrepasar los límites físicos del inventario (ej. > 50,000 unidades).
4. **Pruebas Parametrizadas:** Uso extensivo de `@ParameterizedTest` y `@ValueSource` para inyectar múltiples datos inválidos (cadenas extrañas, códigos mal formateados) en un solo bloque de prueba.
5. **Entornos Aislados:** Uso del ciclo de vida `@BeforeEach` para reiniciar el objeto `Producto` en memoria antes de cada evaluación (Principio de Aislamiento).
6. **Legibilidad:** Documentación de tests en lenguaje natural usando `@DisplayName`.
7. **Aserción Estricta:** Uso de `assertThrows` acoplado con `assertEquals` para capturar y validar sintácticamente cada mensaje de error arrojado por el sistema.

---

## 🚀 ¿Cómo Ejecutar el Proyecto?

### 1. Ejecutar las Pruebas Unitarias (Tests)
La forma más recomendada y sencilla de ejecutar y verificar la suite de 34 pruebas es **a través de tu IDE** (Visual Studio Code, IntelliJ IDEA o Eclipse):
* **En VS Code:** Abre la pestaña de *Testing* (ícono de matraz) en la barra lateral izquierda y presiona el botón **Run Tests** (Play).
* **Para ver el 100% de Cobertura:** En la misma pestaña de *Testing*, haz clic en el botón **Run Tests with Coverage** (ícono de escudo o barras). Se abrirá un reporte detallando el 100% alcanzado en el dominio.

*Alternativa por Consola (Si tienes Maven instalado):*
```bash
mvn clean test
```

### 2. Ejecutar la Aplicación (Interfaz Gráfica)
Si deseas ver el funcionamiento visual del programa:
* **Desde el IDE:** Abre el archivo `App.java` y dale al botón de *Run* o *Play* situado sobre el método `main`.
* **Desde Consola (Maven):**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="pe.com.lab04.App"
```

---

## 🛡️ Reglas de Negocio Validadas

El sistema blinda la base de datos aplicando restricciones como:
* El identificador debe cumplir la RegEx `^P\d{3}$` (Ej: `P001`, `P120`).
* Los nombres no permiten caracteres especiales ni longitudes atípicas (Límites: 2 a 40 chars).
* Precios lógicos: (Mayor a `0` y máximo `S/ 10,000`).
* Límites logísticos de almacén: (Máximo `5,000` unidades por movimiento de Entrada/Salida).
* Límites físicos de servidor: (Capacidad absoluta de almacén fijada en `50,000` unidades).
* La lista de Kárdex histórico es inmutable para prevenir manipulación (Listas defensivas).
