package com.tienda.modelo;

import com.tienda.servicio.ServicioPrecio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoCompraTest {

    @Mock
    private ServicioPrecio servicioPrecioMock;

    private CarritoCompra carrito;
    private Producto productoA;
    private Producto productoB;
    private Producto productoNoDisp;

    @BeforeEach
    void setUp() {
        carrito = new CarritoCompra(servicioPrecioMock);
        productoA = new Producto("P1", "Laptop", 1000.0, true);
        productoB = new Producto("P2", "Mouse", 50.0, true);
        productoNoDisp = new Producto("P3", "Teclado", 10.0, false);
    }

    @Nested
    class ConstructorYValidaciones {
        @Test
        void servicioNulo_LanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> new CarritoCompra(null));
        }
    }

    //  Pruebas sin mocks: operaciones básicas de carrito
    //  Validar producto duplicado
    @Nested
    class OperacionesBasicasSinMocks {
        
        @Test
        void agregarProducto_ValidacionesLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(null, 1));
            assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(productoA, 0));
            assertThrows(IllegalArgumentException.class, () -> carrito.agregarProducto(productoA, -1));
            assertThrows(IllegalStateException.class, () -> carrito.agregarProducto(productoNoDisp, 1));
        }

        @Test
        void agregarProducto_NuevoYDuplicado_FuncionaCorrectamente() {
            // Agrega nuevo
            carrito.agregarProducto(productoA, 1);
            assertEquals(1, carrito.getCantidadItems());
            
            // Validar producto duplicado
            carrito.agregarProducto(productoA, 2);
            assertEquals(1, carrito.getCantidadItems(), "No debe crear un nuevo item");
            assertEquals(3, carrito.getCantidadTotalProductos(), "Debe sumar las cantidades");
        }

        @Test
        void removerProducto_ValidacionesLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> carrito.removerProducto(null));
            assertThrows(IllegalArgumentException.class, () -> carrito.removerProducto("   "));
            assertThrows(IllegalArgumentException.class, () -> carrito.removerProducto("INEXISTENTE"));
        }

        @Test
        void removerProducto_Existente_LoElimina() {
            carrito.agregarProducto(productoA, 1);
            carrito.agregarProducto(productoB, 1);
            
            carrito.removerProducto("P2"); // Remueve el segundo para iterar el bucle for
            
            assertEquals(1, carrito.getCantidadItems());
            assertFalse(carrito.contieneProducto("P2"));
        }

        @Test
        void actualizarCantidad_ValidacionesLanzaExcepcion() {
            assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad(null, 5));
            assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad("   ", 5));
            assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad("P1", 0));
            assertThrows(IllegalArgumentException.class, () -> carrito.actualizarCantidad("INEXISTENTE", 5));
        }

        @Test
        void actualizarCantidad_Existente_Actualiza() {
            carrito.agregarProducto(productoA, 1);
            carrito.actualizarCantidad("P1", 5);
            assertEquals(5, carrito.getCantidadTotalProductos());
        }
    }

    //  Pruebas con Mockito: ServicioPrecio simulado
    //  Validar cálculo correcto de total con impuesto / descuento
    //  Validar carrito vacío (total = 0)
    //  Verificar llamadas a ServicioPrecio
    @Nested
    class CalculosConMockito {

        @Test
        void calcularTotal_CarritoVacio_RetornaCeroYVerificaMocks() {
            // Validar carrito vacío (total = 0)
            when(servicioPrecioMock.calcularDescuento(0.0)).thenReturn(0.0);
            when(servicioPrecioMock.calcularImpuesto(0.0)).thenReturn(0.0);

            assertEquals(0.0, carrito.calcularTotal());
            
            // Verificar llamadas a ServicioPrecio
            verify(servicioPrecioMock).calcularDescuento(0.0);
            verify(servicioPrecioMock).calcularImpuesto(0.0);
        }

        @Test
        void calcularTotal_ConProductos_AplicaDescuentoEImpuesto() {
            carrito.agregarProducto(productoA, 1); // Subtotal 1000

            // Validar cálculo correcto de total con descuento e impuesto
            when(servicioPrecioMock.calcularDescuento(1000.0)).thenReturn(100.0);
            when(servicioPrecioMock.calcularImpuesto(900.0)).thenReturn(162.0);

            assertEquals(1062.0, carrito.calcularTotal());
            
            verify(servicioPrecioMock, times(1)).calcularDescuento(1000.0);
            verify(servicioPrecioMock, times(1)).calcularImpuesto(900.0);
        }

        @Test
        void obtenerResumenCompra_CarritoVacio_RetornaMensaje() {
            assertEquals("Carrito vacio", carrito.obtenerResumenCompra());
        }

        @Test
        void obtenerResumenCompra_ConProductos_ConstruyeResumen() {
            carrito.agregarProducto(productoA, 1);
            
            when(servicioPrecioMock.calcularDescuento(anyDouble())).thenReturn(100.0);
            when(servicioPrecioMock.calcularImpuesto(anyDouble())).thenReturn(162.0);

            String resumen = carrito.obtenerResumenCompra();

            assertTrue(resumen.contains("=== RESUMEN DE COMPRA ==="));
            assertTrue(resumen.contains("Laptop"));
            assertTrue(resumen.contains("TOTAL:"));
        }
    }

    //  Pruebas parametrizadas para diferentes montos
    @Nested
    class PruebasParametrizadas {
        
        @ParameterizedTest(name = "Subtotal: {0}, Desc: {1}, Imp: {2} => Total: {3}")
        @CsvSource({
            "100.0,   10.0,   16.2,   106.2",
            "500.0,   50.0,   81.0,   531.0",
            "2000.0, 200.0,  324.0,  2124.0"
        })
        void calcularTotal_DiferentesMontos(double precio, double desc, double imp, double totalEsperado) {
            Producto p = new Producto("PX", "Parametrizado", precio, true);
            carrito.agregarProducto(p, 1);
            
            when(servicioPrecioMock.calcularDescuento(precio)).thenReturn(desc);
            when(servicioPrecioMock.calcularImpuesto(precio - desc)).thenReturn(imp);

            assertEquals(totalEsperado, carrito.calcularTotal(), 0.001);
        }
    }

    //  Casos límite: carrito con 1 producto, con 100 productos
    // Y cobertura de getters restantes
    @Nested
    class CasosLimiteYConsultas {

        @Test
        void casosLimite_UnoYCienProductos() {
            // Caso límite: carrito con 1 producto
            carrito.agregarProducto(productoA, 1);
            assertEquals(1, carrito.getCantidadTotalProductos());
            
            carrito.vaciar();
            
            // Caso límite: carrito con 100 productos
            carrito.agregarProducto(productoB, 100);
            assertEquals(100, carrito.getCantidadTotalProductos());
        }

        @Test
        void consultasAdicionales_ParaCoberturaTotal() {
            carrito.agregarProducto(productoA, 1);
            
            // getItems
            List<ItemCarrito> lista = carrito.getItems();
            assertEquals(1, lista.size());
            
            // contieneProducto
            assertTrue(carrito.contieneProducto("P1"));
            assertFalse(carrito.contieneProducto("P99"));
        }

        @Test
        void obtenerItemConMayorSubtotal_VacioYConElementos() {
            // Vacío
            assertFalse(carrito.obtenerItemConMayorSubtotal().isPresent());
            
            // Con elementos
            carrito.agregarProducto(productoB, 10); // Subtotal 500
            carrito.agregarProducto(productoA, 1);  // Subtotal 1000
            
            Optional<ItemCarrito> mayor = carrito.obtenerItemConMayorSubtotal();
            assertTrue(mayor.isPresent());
            assertEquals("P1", mayor.get().getProducto().getId());
        }
    }
}