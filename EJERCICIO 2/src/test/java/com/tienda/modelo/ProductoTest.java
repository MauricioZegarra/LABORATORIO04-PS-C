package com.tienda.modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductoTest {

    // --- RAMAS DEL CONSTRUCTOR ---

    @Test
    void constructor_IdNulo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Producto(null, "Laptop", 1000.0, true));
    }

    @Test
    void constructor_IdVacioOEspacios_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Producto("   ", "Laptop", 1000.0, true));
    }

    @Test
    void constructor_NombreNulo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Producto("P1", null, 1000.0, true));
    }

    @Test
    void constructor_NombreVacioOEspacios_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Producto("P1", "   ", 1000.0, true));
    }

    @Test
    void constructor_PrecioNegativo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Producto("P1", "Laptop", -10.0, true));
    }

    @Test
    void constructor_DatosValidos_CreaProductoYAsignaValores() {
        Producto p = new Producto("P1", "Laptop", 1500.0, true);
        
        assertEquals("P1", p.getId());
        assertEquals("Laptop", p.getNombre());
        assertEquals(1500.0, p.getPrecio());
        assertTrue(p.isDisponible());
    }

    // --- RAMAS DEL MÉTODO EQUALS ---

    @Test
    void equals_MismaInstancia_RetornaTrue() {
        Producto p = new Producto("P1", "Laptop", 1500.0, true);
        assertTrue(p.equals(p));
    }

    @Test
    void equals_ComparadoConNull_RetornaFalse() {
        Producto p = new Producto("P1", "Laptop", 1500.0, true);
        assertFalse(p.equals(null));
    }

    @Test
    void equals_ComparadoConOtraClase_RetornaFalse() {
        Producto p = new Producto("P1", "Laptop", 1500.0, true);
        assertFalse(p.equals(new Object()));
    }

    @Test
    void equals_MismoIdDiferentesDatos_RetornaTrue() {
        Producto p1 = new Producto("P1", "Laptop", 1500.0, true);
        Producto p2 = new Producto("P1", "Mouse", 20.0, false);
        assertTrue(p1.equals(p2));
    }

    @Test
    void equals_DiferenteId_RetornaFalse() {
        Producto p1 = new Producto("P1", "Laptop", 1500.0, true);
        Producto p2 = new Producto("P2", "Laptop", 1500.0, true);
        assertFalse(p1.equals(p2));
    }

    // --- MÉTODOS ADICIONALES ---

    @Test
    void hashCode_ProductosConMismoId_RetornanMismoHash() {
        Producto p1 = new Producto("P1", "Laptop", 1500.0, true);
        Producto p2 = new Producto("P1", "Mouse", 20.0, false);
        
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void toString_RetornaFormatoEsperado() {
        Producto p = new Producto("P1", "Laptop", 1500.50, true);
        String esperado = "Producto{id='P1', nombre='Laptop', precio=1500.50, disponible=true}";
        
        assertEquals(esperado, p.toString());
    }
}