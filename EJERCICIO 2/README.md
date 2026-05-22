# 🛒 Carrito de Compras - Proyecto Final

Este proyecto consiste en una aplicación de escritorio desarrollada en **Java Swing** que simula el funcionamiento básico de un sistema de carrito de compras.

El sistema permite gestionar productos dentro del carrito, realizar operaciones de compra y validar el correcto funcionamiento mediante pruebas automatizadas utilizando **JUnit 5**, **Mockito** y análisis de cobertura con **JaCoCo**.

---

## 📌 Características Principales

- Interfaz gráfica desarrollada con **Java Swing**.
- Gestión de productos dentro del carrito de compras.
- Arquitectura basada en Maven.
- Ejecución de pruebas unitarias automatizadas.
- Uso de mocks para simulación de comportamientos con Mockito.
- Generación de reportes de cobertura de código con JaCoCo.
- Compatible con Java 17.

---

## 🛠️ Tecnologías Utilizadas

| Tecnología | Descripción |
|---|---|
| Java 17 | Lenguaje principal del proyecto |
| Java Swing | Interfaz gráfica de usuario |
| Apache Maven | Gestión de dependencias y compilación |
| JUnit 5 | Framework de pruebas unitarias |
| Mockito | Simulación y pruebas de comportamiento |
| JaCoCo | Cobertura de pruebas |

---

## 📂 Estructura del Proyecto

```text
carrito-compras/
├── src/
│   ├── main/java/com/tienda/
│   │   └── Main.java
│   │
│   └── test/java/com/tienda/
│
├── target/
│   ├── carrito-compras-1.0-SNAPSHOT.jar
│   └── jacoco.exec
│
├── pom.xml
└── README.md
```

---

## ▶️ Ejecución del Proyecto

### 1. Clonar el repositorio

```bash
git clone <URL_DEL_REPOSITORIO>
cd carrito-compras
```

---

### 2. Compilar el proyecto

```bash
mvn clean compile
```

---

### 3. Ejecutar la aplicación

```bash
mvn exec:java
```

También puede ejecutarse directamente desde el IDE ejecutando la clase:

```text
com.tienda.Main
```

---

## 🧪 Ejecución de Pruebas

Para ejecutar todas las pruebas unitarias:

```bash
mvn test
```

---

## 📊 Cobertura de Código con JaCoCo

El proyecto incluye integración con JaCoCo para generar reportes de cobertura.

Para generar el reporte:

```bash
mvn clean test
```

El reporte HTML se encontrará en:

```text
target/site/jacoco/index.html
```

---

## ⚙️ Dependencias Principales

El proyecto utiliza las siguientes dependencias principales:

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.5.0</version>
    <scope>test</scope>
</dependency>
```

---

## 🖥️ Arquitectura General

El flujo principal de la aplicación inicia desde la clase `Main.java`, donde se crea la interfaz gráfica utilizando `SwingUtilities.invokeLater()` para garantizar una ejecución segura en el hilo de eventos de Swing.

```java
SwingUtilities.invokeLater(() -> {
    CarritoUI frame = new CarritoUI();
    frame.setVisible(true);
});
```

---

## 📖 Objetivo del Proyecto

El propósito principal de este proyecto es aplicar conceptos de:

- Desarrollo de interfaces gráficas.
- Programación orientada a objetos.
- Gestión de dependencias con Maven.
- Automatización de pruebas.
- Cobertura y calidad de software.

---

## 👨‍💻 Autor

Proyecto académico desarrollado para prácticas y evaluación de pruebas de software y desarrollo Java.
