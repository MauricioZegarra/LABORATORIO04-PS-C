# 🛒 Carrito de Compras - Ejercicio 2 (Pruebas de Software)

Este repositorio contiene la solución del **Ejercicio Propuesto 2: Carrito de Compras**, desarrollada en Java y enfocada en la validación del comportamiento del negocio mediante **pruebas unitarias**.

El proyecto modela un carrito de compras capaz de **agregar productos, actualizar cantidades, remover ítems, vaciar el carrito y calcular subtotales, descuentos e impuestos**. Además, registra un historial de operaciones para dejar trazabilidad de cada acción ejecutada.

---

## 🛠️ Tecnologías y Herramientas

- **Lenguaje:** Java 17
- **Gestor de dependencias:** Apache Maven
- **Framework de pruebas:** JUnit 5
- **Mocking:** Mockito
- **Cobertura:** JaCoCo
- **Interfaz gráfica:** Java Swing

---

## 🧱 Estructura del Proyecto

La solución sigue la estructura estándar de Maven:

```text
carrito-compras/
├── src/
│   ├── main/java/com/tienda/
│   │   ├── Main.java
│   │   ├── modelo/
│   │   │   ├── Producto.java
│   │   │   ├── ItemCarrito.java
│   │   │   ├── CarritoCompra.java
│   │   │   └── HistorialOperacion.java
│   │   ├── servicio/
│   │   │   ├── ServicioPrecio.java
│   │   │   └── ServicioPrecioImpl.java
│   │   └── ui/
│   │       └── CarritoUI.java
│   └── test/java/com/tienda/
│       ├── modelo/
│       │   ├── ProductoTest.java
│       │   ├── ItemCarritoTest.java
│       │   └── CarritoCompraTest.java
│       └── servicio/
│           └── ServicioPrecioImplTest.java
└── pom.xml
```

---

## 🧪 Enfoque de Pruebas

La parte más importante del proyecto es la validación del núcleo de negocio. Para ello se implementó una suite de **52 pruebas unitarias** que cubre constructor, validaciones, cálculos, comportamiento de colecciones y formato de salida.

### Lo que se valida
- creación de productos e ítems del carrito,
- manejo de cantidades inválidas,
- agregado de productos nuevos y duplicados,
- eliminación y actualización de productos,
- cálculo de subtotal y total,
- aplicación de descuento e impuesto,
- historial de operaciones,
- métodos `equals`, `hashCode` y `toString`.

### Recursos usados en las pruebas
- `@BeforeEach` para aislar cada caso,
- `@Nested` para organizar escenarios,
- `@ParameterizedTest` donde ayuda a simplificar validaciones,
- Mockito para simular el servicio de precios.

---

## ✅ Funcionalidades Principales

### Producto
Representa un artículo disponible para compra, con:
- identificador,
- nombre,
- precio,
- estado de disponibilidad.

### ItemCarrito
Modela un producto dentro del carrito junto con su cantidad. Permite:
- obtener subtotal,
- cambiar cantidad,
- incrementar unidades.

### CarritoCompra
Es la clase central del sistema. Permite:
- agregar productos,
- evitar duplicados sumando cantidades,
- remover productos por ID,
- actualizar cantidades,
- vaciar el carrito,
- calcular subtotal y total,
- consultar historial,
- obtener el item con mayor subtotal.

### ServicioPrecio
Encapsula la lógica de cálculo de:
- descuento,
- impuesto.

### HistorialOperacion
Registra cada movimiento importante del carrito con fecha, tipo y detalle.

---

## 🚀 Cómo Ejecutar el Proyecto

### Ejecutar las pruebas
Desde consola:

```bash
mvn clean test
```

### Ejecutar la aplicación
Compilar y ejecutar la interfaz:

```bash
mvn clean compile
mvn exec:java
```

Si tu IDE lo permite, también puedes ejecutar directamente la clase `com.tienda.Main`.

---

## 📋 Reglas de Negocio Implementadas

- El carrito no acepta un servicio de precios nulo.
- No se pueden agregar productos nulos.
- La cantidad debe ser mayor que cero.
- No se permite agregar productos no disponibles.
- Si un producto ya existe en el carrito, se incrementa su cantidad en lugar de crear otro ítem.
- No se permiten montos negativos para descuento o impuesto.
- El historial guarda cada operación realizada.

---

## 📌 Resultado del Ejercicio

Este ejercicio está orientado a demostrar una implementación clara de lógica de negocio junto con una estrategia de pruebas sólida. La prioridad está en la **correctitud, mantenibilidad y verificabilidad** del carrito de compras.
