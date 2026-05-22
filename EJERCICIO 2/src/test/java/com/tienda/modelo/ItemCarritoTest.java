package com.tienda.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ItemCarritoTest {

    private Producto productoValido;

    @BeforeEach
    void setUp() {
        // Objeto de apoyo para las pruebas, se reinicia antes de cada @Test
        productoValido = new Producto("P1", "Monitor", 200.0, true);
    }

    // --- RAMAS DEL CONSTRUCTOR ---

    @Test
    void constructor_ProductoNulo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ItemCarrito(null, 2));
    }

    @Test
    void constructor_CantidadCero_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ItemCarrito(productoValido, 0));
    }

    @Test
    void constructor_CantidadNegativa_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ItemCarrito(productoValido, -1));
    }

    @Test
    void constructor_DatosValidos_CreaItemYAsignaValores() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        assertEquals(productoValido, item.getProducto());
        assertEquals(2, item.getCantidad());
    }

    // --- RAMAS DE setCantidad ---

    @Test
    void setCantidad_CantidadCero_LanzaExcepcion() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        assertThrows(IllegalArgumentException.class, 
            () -> item.setCantidad(0));
    }

    @Test
    void setCantidad_CantidadNegativa_LanzaExcepcion() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        assertThrows(IllegalArgumentException.class, 
            () -> item.setCantidad(-5));
    }

    @Test
    void setCantidad_CantidadValida_ActualizaValor() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        item.setCantidad(5);
        
        assertEquals(5, item.getCantidad());
    }

    // --- RAMAS DE incrementarCantidad ---

    @Test
    void incrementarCantidad_IncrementoCero_LanzaExcepcion() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        assertThrows(IllegalArgumentException.class, 
            () -> item.incrementarCantidad(0));
    }

    @Test
    void incrementarCantidad_IncrementoNegativo_LanzaExcepcion() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        assertThrows(IllegalArgumentException.class, 
            () -> item.incrementarCantidad(-2));
    }

    @Test
    void incrementarCantidad_IncrementoValido_SumaAlValorActual() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        item.incrementarCantidad(3); // 2 + 3 = 5
        
        assertEquals(5, item.getCantidad());
    }

    // --- MÉTODOS DE CÁLCULO Y TEXTO ---

    @Test
    void getSubtotal_MultiplicaPrecioPorCantidad() {
        // El precio del producto válido es 200.0, con cantidad 3 esperamos 600.0
        ItemCarrito item = new ItemCarrito(productoValido, 3);
        
        assertEquals(600.0, item.getSubtotal());
    }

    @Test
    void toString_RetornaFormatoEsperado() {
        ItemCarrito item = new ItemCarrito(productoValido, 2);
        
        // Usamos String.format en la prueba para evitar fallos si el sistema operativo 
        // usa comas (,) en lugar de puntos (.) para los decimales (Locale dependency)
        String esperado = String.format("Monitor x2 - $%.2f", 400.0);
        
        assertEquals(esperado, item.toString());
    }
}