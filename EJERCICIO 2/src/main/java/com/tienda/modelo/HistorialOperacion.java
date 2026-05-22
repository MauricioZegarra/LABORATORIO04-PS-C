package com.tienda.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialOperacion {
    private final LocalDateTime fecha;
    private final String tipoOperacion;
    private final String detalle;

    public HistorialOperacion(String tipoOperacion, String detalle) {
        this.fecha = LocalDateTime.now();
        this.tipoOperacion = tipoOperacion;
        this.detalle = detalle;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public String getDetalle() {
        return detalle;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s: %s", 
                           fecha.format(formatter), tipoOperacion, detalle);
    }
}
