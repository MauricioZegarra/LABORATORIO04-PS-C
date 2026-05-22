package pe.com.lab04;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Movimiento {
    private final TipoMovimiento tipo;
    private final int cantidad;
    private final LocalDateTime fecha;

    public Movimiento(TipoMovimiento tipo, int cantidad) {
        if (tipo == null) {
            throw new InventarioException("El tipo de movimiento no puede ser nulo.");
        }

        if (cantidad <= 0) {
            throw new InventarioException("La cantidad del movimiento debe ser mayor que cero.");
        }

        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fecha.format(formato) + " | " + tipo + " | Cantidad: " + cantidad + " unidades";
    }
}