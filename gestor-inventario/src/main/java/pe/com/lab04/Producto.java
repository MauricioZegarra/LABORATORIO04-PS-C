package pe.com.lab04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Producto {

    private String codigo;
    private String nombre;
    private double precio;
    private int cantidad;
    private final List<Movimiento> movimientos;

    public Producto(String codigo, String nombre, double precio, int cantidad) {
        validarCodigo(codigo);
        validarNombre(nombre);
        validarPrecio(precio);
        validarCantidadInicial(cantidad);

        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.precio = precio;
        this.cantidad = cantidad;
        this.movimientos = new ArrayList<>();

        if (cantidad > 0) {
            movimientos.add(new Movimiento(TipoMovimiento.ENTRADA, cantidad));
        }
    }

    public void agregarStock(int cantidad) {
        validarCantidadMovimiento(cantidad);

        if (this.cantidad + cantidad > 50000) {
            throw new InventarioException("El stock total no puede superar las 50000 unidades.");
        }

        this.cantidad += cantidad;
        movimientos.add(new Movimiento(TipoMovimiento.ENTRADA, cantidad));
    }

    public void extraerStock(int cantidad) {
        validarCantidadMovimiento(cantidad);

        if (cantidad > this.cantidad) {
            throw new InventarioException(
                    "Stock insuficiente. Disponible: " + this.cantidad + ", solicitado: " + cantidad
            );
        }

        this.cantidad -= cantidad;
        movimientos.add(new Movimiento(TipoMovimiento.SALIDA, cantidad));
    }

    public int consultarStock() {
        return cantidad;
    }

    public double obtenerValorTotal() {
        return precio * cantidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    private void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new InventarioException("El código del producto no puede estar vacío.");
        }

        if (!codigo.trim().toUpperCase().matches("^P\\d{3}$")) {
            throw new InventarioException("El código debe tener el formato P001, P002, P120, etc.");
        }
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new InventarioException("El nombre del producto no puede estar vacío.");
        }

        if (nombre.trim().length() < 2 || nombre.trim().length() > 40) {
            throw new InventarioException("El nombre debe tener entre 2 y 40 caracteres.");
        }

        if (!nombre.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            throw new InventarioException("El nombre solo debe contener letras, números y espacios.");
        }
    }

    private void validarPrecio(double precio) {
        if (precio <= 0) {
            throw new InventarioException("El precio del producto debe ser mayor que cero.");
        }

        if (precio > 10000) {
            throw new InventarioException("El precio no puede ser mayor a S/ 10000.");
        }
    }

    private void validarCantidadInicial(int cantidad) {
        if (cantidad < 0) {
            throw new InventarioException("La cantidad inicial no puede ser negativa.");
        }

        if (cantidad > 10000) {
            throw new InventarioException("El stock inicial no puede ser mayor a 10000 unidades.");
        }
    }

    private void validarCantidadMovimiento(int cantidad) {
        if (cantidad <= 0) {
            throw new InventarioException("La cantidad debe ser mayor que cero.");
        }

        if (cantidad > 5000) {
            throw new InventarioException("No se puede mover más de 5000 unidades en una sola operación.");
        }
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " | Precio: S/ " + precio + " | Stock: " + cantidad + " unidades";
    }
}
