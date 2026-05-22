package com.tienda.servicio;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServicioPrecioImplTest {

    // --- RAMAS DE LOS CONSTRUCTORES ---

    @Test
    void constructorParametrizado_DescuentoNegativo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ServicioPrecioImpl(-0.1, 0.18));
    }

    @Test
    void constructorParametrizado_DescuentoMayorAUno_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ServicioPrecioImpl(1.1, 0.18));
    }

    @Test
    void constructorParametrizado_ImpuestoNegativo_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, 
            () -> new ServicioPrecioImpl(0.1, -0.05));
    }

    @Test
    void constructorParametrizado_ValoresValidos_AsignaPropiedades() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl(0.15, 0.21);
        
        // Verifica que los getters retornen lo asignado en el constructor
        assertEquals(0.15, servicio.getTasaDescuento());
        assertEquals(0.21, servicio.getTasaImpuesto());
    }

    @Test
    void constructorPorDefecto_AsignaValoresPredefinidos() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl();
        
        // Verifica que el constructor sin parámetros asigne 0.10 y 0.18
        assertEquals(0.10, servicio.getTasaDescuento());
        assertEquals(0.18, servicio.getTasaImpuesto());
    }

    // --- RAMAS DE calcularDescuento ---

    @Test
    void calcularDescuento_MontoNegativo_LanzaExcepcion() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl();
        
        assertThrows(IllegalArgumentException.class, 
            () -> servicio.calcularDescuento(-100.0));
    }

    @Test
    void calcularDescuento_MontoCero_RetornaCero() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl(0.10, 0.18);
        
        assertEquals(0.0, servicio.calcularDescuento(0.0));
    }

    @Test
    void calcularDescuento_MontoPositivo_CalculaCorrectamente() {
        // Tasa de descuento 20% (0.20)
        ServicioPrecioImpl servicio = new ServicioPrecioImpl(0.20, 0.18);
        
        // El 20% de 500 debe ser 100
        assertEquals(100.0, servicio.calcularDescuento(500.0));
    }

    // --- RAMAS DE calcularImpuesto ---

    @Test
    void calcularImpuesto_MontoNegativo_LanzaExcepcion() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl();
        
        assertThrows(IllegalArgumentException.class, 
            () -> servicio.calcularImpuesto(-50.0));
    }

    @Test
    void calcularImpuesto_MontoCero_RetornaCero() {
        ServicioPrecioImpl servicio = new ServicioPrecioImpl(0.10, 0.18);
        
        assertEquals(0.0, servicio.calcularImpuesto(0.0));
    }

    @Test
    void calcularImpuesto_MontoPositivo_CalculaCorrectamente() {
        // Tasa de impuesto 18% (0.18)
        ServicioPrecioImpl servicio = new ServicioPrecioImpl(0.10, 0.18);
        
        // El 18% de 200 debe ser 36
        assertEquals(36.0, servicio.calcularImpuesto(200.0));
    }
}