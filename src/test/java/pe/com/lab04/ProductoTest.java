package pe.com.lab04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas Unitarias para la clase Producto")
public class ProductoTest {

    private Producto producto;

    @BeforeEach
    void setUp() {
        // Se ejecuta antes de cada prueba
        producto = new Producto("P001", "Laptop HP", 2500.0, 10);
    }

    // ---------------------------------------------------------
    // 1. Pruebas de Creación y Getters (Casos de Éxito)
    // ---------------------------------------------------------

    @Test
    @DisplayName("1. Debería crear un producto exitosamente con datos válidos")
    void testCreacionExitosaProducto() {
        assertNotNull(producto);
        assertEquals("P001", producto.getCodigo());
        assertEquals("Laptop HP", producto.getNombre());
        assertEquals(2500.0, producto.getPrecio());
        assertEquals(10, producto.getCantidad());
    }

    @Test
    @DisplayName("2. consultarStock debe retornar el stock actual")
    void testConsultarStockInicial() {
        assertEquals(10, producto.consultarStock());
    }

    @Test
    @DisplayName("3. obtenerValorTotal debe calcular correctamente precio * cantidad")
    void testObtenerValorTotalCorrecto() {
        assertEquals(25000.0, producto.obtenerValorTotal());
    }

    // ---------------------------------------------------------
    // 2. Pruebas de Validación de Código (Excepciones)
    // ---------------------------------------------------------

    @Test
    @DisplayName("4. Debería lanzar excepción si el código es nulo")
    void testValidarCodigoNuloLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto(null, "Mouse", 50.0, 5);
        });
        assertEquals("El código del producto no puede estar vacío.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n"})
    @DisplayName("5. Debería lanzar excepción si el código está vacío o son espacios")
    void testValidarCodigoVacioLanzaExcepcion(String codigoInvalido) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto(codigoInvalido, "Mouse", 50.0, 5);
        });
        assertEquals("El código del producto no puede estar vacío.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"P12", "P1234", "A001", "001", "X001"})
    @DisplayName("6. Debería lanzar excepción si el código tiene formato inválido")
    void testValidarCodigoFormatoInvalidoLanzaExcepcion(String codigoInvalido) {
        // La validación ocurre ANTES del trim y toUpperCase, según el código original
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto(codigoInvalido, "Mouse", 50.0, 5);
        });
        assertEquals("El código debe tener el formato P001, P002, P120, etc.", exception.getMessage());
    }

    @Test
    @DisplayName("7. Debería aceptar un código en minúscula y convertirlo a mayúscula")
    void testCodigoMinusculaSeConvierteAMayuscula() {
        Producto p = new Producto("p999", "Teclado", 100, 5);
        assertEquals("P999", p.getCodigo());
    }

    // ---------------------------------------------------------
    // 3. Pruebas de Validación de Nombre (Excepciones)
    // ---------------------------------------------------------

    @Test
    @DisplayName("8. Debería lanzar excepción si el nombre es nulo")
    void testValidarNombreNuloLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P002", null, 50.0, 5);
        });
        assertEquals("El nombre del producto no puede estar vacío.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("9. Debería lanzar excepción si el nombre está vacío o son espacios")
    void testValidarNombreVacioLanzaExcepcion(String nombreInvalido) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P002", nombreInvalido, 50.0, 5);
        });
        assertEquals("El nombre del producto no puede estar vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("10. Debería lanzar excepción si el nombre tiene menos de 2 caracteres")
    void testValidarNombreCortoLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P002", "A", 50.0, 5);
        });
        assertEquals("El nombre debe tener entre 2 y 40 caracteres.", exception.getMessage());
    }

    @Test
    @DisplayName("11. Debería lanzar excepción si el nombre tiene más de 40 caracteres")
    void testValidarNombreLargoLanzaExcepcion() {
        String nombreLargo = "Monitor Ultra Wide Curvo 4K Samsung Odyssey G9"; // 46 chars
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P002", nombreLargo, 50.0, 5);
        });
        assertEquals("El nombre debe tener entre 2 y 40 caracteres.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Laptop@HP", "Mouse_Gamer", "Monitor-Dell", "Teclado RGB!"})
    @DisplayName("12. Debería lanzar excepción si el nombre contiene caracteres inválidos")
    void testValidarNombreCaracteresInvalidosLanzaExcepcion(String nombreInvalido) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P002", nombreInvalido, 50.0, 5);
        });
        assertEquals("El nombre solo debe contener letras, números y espacios.", exception.getMessage());
    }

    // ---------------------------------------------------------
    // 4. Pruebas de Validación de Precio y Cantidad Inicial (Excepciones)
    // ---------------------------------------------------------

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.5, -0.01})
    @DisplayName("13. Debería lanzar excepción si el precio es cero o negativo")
    void testValidarPrecioCeroONegativoLanzaExcepcion(double precioInvalido) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P003", "Audifonos", precioInvalido, 5);
        });
        assertEquals("El precio del producto debe ser mayor que cero.", exception.getMessage());
    }

    @Test
    @DisplayName("14. Debería lanzar excepción si el precio supera los S/ 10000")
    void testValidarPrecioExcesivoLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P003", "MacBook Pro M3 Max", 15000.0, 5);
        });
        assertEquals("El precio no puede ser mayor a S/ 10000.", exception.getMessage());
    }

    @Test
    @DisplayName("15. Debería lanzar excepción si la cantidad inicial es negativa")
    void testValidarCantidadInicialNegativaLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P004", "Impresora", 500.0, -5);
        });
        assertEquals("La cantidad inicial no puede ser negativa.", exception.getMessage());
    }

    @Test
    @DisplayName("16. Debería lanzar excepción si la cantidad inicial supera las 10000 unidades")
    void testValidarCantidadInicialExcesivaLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Producto("P004", "Impresora", 500.0, 15000);
        });
        assertEquals("El stock inicial no puede ser mayor a 10000 unidades.", exception.getMessage());
    }

    // ---------------------------------------------------------
    // 5. Pruebas de Movimientos: Agregar Stock
    // ---------------------------------------------------------

    @Test
    @DisplayName("17. agregarStock debería incrementar la cantidad correctamente")
    void testAgregarStockCorrectamente() {
        producto.agregarStock(20);
        assertEquals(30, producto.getCantidad());
        assertEquals(75000.0, producto.obtenerValorTotal());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5, -100})
    @DisplayName("18. agregarStock debería lanzar excepción si la cantidad es cero o negativa")
    void testAgregarStockInvalidoLanzaExcepcion(int cantidad) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            producto.agregarStock(cantidad);
        });
        assertEquals("La cantidad debe ser mayor que cero.", exception.getMessage());
    }

    @Test
    @DisplayName("19. agregarStock debería lanzar excepción si excede el límite por operación (5000)")
    void testAgregarStockExcedeLimiteMovimientoLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            producto.agregarStock(5001);
        });
        assertEquals("No se puede mover más de 5000 unidades en una sola operación.", exception.getMessage());
    }

    @Test
    @DisplayName("20. agregarStock debería lanzar excepción si el stock total supera las 50000 unidades")
    void testAgregarStockExcedeLimiteTotalLanzaExcepcion() {
        Producto p = new Producto("P005", "Tornillos", 0.5, 9000);
        // Hacemos que llegue casi al limite
        p.agregarStock(5000); p.agregarStock(5000); p.agregarStock(5000);
        p.agregarStock(5000); p.agregarStock(5000); p.agregarStock(5000);
        p.agregarStock(5000); p.agregarStock(5000); // 9000 + 40000 = 49000
        
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            p.agregarStock(1001); // 49000 + 1001 = 50001 (Falla)
        });
        assertEquals("El stock total no puede superar las 50000 unidades.", exception.getMessage());
    }

    // ---------------------------------------------------------
    // 6. Pruebas de Movimientos: Extraer Stock
    // ---------------------------------------------------------

    @Test
    @DisplayName("21. extraerStock debería decrementar la cantidad correctamente")
    void testExtraerStockCorrectamente() {
        producto.extraerStock(5);
        assertEquals(5, producto.getCantidad());
    }

    @Test
    @DisplayName("22. extraerStock debería lanzar excepción si no hay stock suficiente")
    void testExtraerStockInsuficienteLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            producto.extraerStock(11);
        });
        assertEquals("Stock insuficiente. Disponible: 10, solicitado: 11", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    @DisplayName("23. extraerStock debería lanzar excepción si la cantidad es cero o negativa")
    void testExtraerStockInvalidoLanzaExcepcion(int cantidad) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            producto.extraerStock(cantidad);
        });
        assertEquals("La cantidad debe ser mayor que cero.", exception.getMessage());
    }

    @Test
    @DisplayName("24. extraerStock debería lanzar excepción si excede el límite por operación (5000)")
    void testExtraerStockExcedeLimiteMovimientoLanzaExcepcion() {
        Producto p = new Producto("P006", "Clavos", 0.1, 8000);
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            p.extraerStock(5001);
        });
        assertEquals("No se puede mover más de 5000 unidades en una sola operación.", exception.getMessage());
    }

    // ---------------------------------------------------------
    // 7. Pruebas de Registro de Movimientos
    // ---------------------------------------------------------

    @Test
    @DisplayName("25. Crear un producto con stock inicial > 0 debería registrar un movimiento de ENTRADA")
    void testRegistraMovimientoInicialAlCrearProducto() {
        assertEquals(1, producto.getMovimientos().size());
        Movimiento m = producto.getMovimientos().get(0);
        assertEquals(TipoMovimiento.ENTRADA, m.getTipo());
        assertEquals(10, m.getCantidad());
        assertNotNull(m.getFecha());
    }

    @Test
    @DisplayName("26. agregarStock debería registrar un nuevo movimiento de ENTRADA")
    void testRegistraMovimientoAlAgregarStock() {
        producto.agregarStock(15);
        assertEquals(2, producto.getMovimientos().size());
        
        Movimiento m = producto.getMovimientos().get(1);
        assertEquals(TipoMovimiento.ENTRADA, m.getTipo());
        assertEquals(15, m.getCantidad());
    }

    @Test
    @DisplayName("27. extraerStock debería registrar un nuevo movimiento de SALIDA")
    void testRegistraMovimientoAlExtraerStock() {
        producto.extraerStock(3);
        assertEquals(2, producto.getMovimientos().size());
        
        Movimiento m = producto.getMovimientos().get(1);
        assertEquals(TipoMovimiento.SALIDA, m.getTipo());
        assertEquals(3, m.getCantidad());
    }

    @Test
    @DisplayName("28. La lista de movimientos debe ser inmodificable")
    void testListaMovimientosEsInmutable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            producto.getMovimientos().add(new Movimiento(TipoMovimiento.ENTRADA, 5));
        });
    }

    @Test
    @DisplayName("29. El método toString de Producto debe tener el formato correcto")
    void testProductoToString() {
        String esperado = "P001 - Laptop HP | Precio: S/ 2500.0 | Stock: 10 unidades";
        assertEquals(esperado, producto.toString());
    }

    @Test
    @DisplayName("30. El método toString de Movimiento debe tener el formato correcto")
    void testMovimientoToString() {
        Movimiento m = producto.getMovimientos().get(0);
        assertTrue(m.toString().contains("ENTRADA | Cantidad: 10 unidades"));
        assertTrue(m.toString().contains("/")); // verifica formato de fecha
    }

    @Test
    @DisplayName("31. Crear producto con stock inicial 0 no debe registrar movimiento de entrada")
    void testCrearProductoConStockCero() {
        Producto pCero = new Producto("P009", "Cable USB", 15.0, 0);
        assertEquals(0, pCero.getCantidad());
        assertEquals(0, pCero.getMovimientos().size()); // No entra al if (cantidad > 0)
    }

    @Test
    @DisplayName("32. Movimiento: Lanza excepción si el tipo es nulo")
    void testMovimientoTipoNuloLanzaExcepcion() {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Movimiento(null, 5);
        });
        assertEquals("El tipo de movimiento no puede ser nulo.", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -5})
    @DisplayName("33. Movimiento: Lanza excepción si la cantidad es cero o negativa")
    void testMovimientoCantidadInvalidaLanzaExcepcion(int cantidad) {
        InventarioException exception = assertThrows(InventarioException.class, () -> {
            new Movimiento(TipoMovimiento.ENTRADA, cantidad);
        });
        assertEquals("La cantidad del movimiento debe ser mayor que cero.", exception.getMessage());
    }

    @Test
    @DisplayName("34. Movimiento: getFecha retorna la fecha correcta")
    void testMovimientoGetFecha() {
        Movimiento m = new Movimiento(TipoMovimiento.SALIDA, 10);
        assertNotNull(m.getFecha());
    }
}
