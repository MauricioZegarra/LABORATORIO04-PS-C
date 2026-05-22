package com.tienda.servicio;

public class ServicioPrecioImpl implements ServicioPrecio {
    
    private final double tasaDescuento;
    private final double tasaImpuesto;

    public ServicioPrecioImpl(double tasaDescuento, double tasaImpuesto) {
        if (tasaDescuento < 0 || tasaDescuento > 1) {
            throw new IllegalArgumentException("La tasa de descuento debe estar entre 0 y 1");
        }
        if (tasaImpuesto < 0) {
            throw new IllegalArgumentException("La tasa de impuesto no puede ser negativa");
        }
        
        this.tasaDescuento = tasaDescuento;
        this.tasaImpuesto = tasaImpuesto;
    }

    public ServicioPrecioImpl() {
        this(0.10, 0.18);
    }

    @Override
    public double calcularDescuento(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        return monto * tasaDescuento;
    }

    @Override
    public double calcularImpuesto(double monto) {
        if (monto < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        return monto * tasaImpuesto;
    }

    public double getTasaDescuento() {
        return tasaDescuento;
    }

    public double getTasaImpuesto() {
        return tasaImpuesto;
    }
}
