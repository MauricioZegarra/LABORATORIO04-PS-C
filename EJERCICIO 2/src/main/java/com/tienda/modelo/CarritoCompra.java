package com.tienda.modelo;

import com.tienda.servicio.ServicioPrecio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoCompra {

    private final List<ItemCarrito> items;
    private final List<HistorialOperacion> historial;
    private final ServicioPrecio servicioPrecio;

    public CarritoCompra(ServicioPrecio servicioPrecio) {
        if (servicioPrecio == null) {
            throw new IllegalArgumentException("El servicio de precio no puede ser nulo");
        }
        this.items = new ArrayList<>();
        this.historial = new ArrayList<>();
        this.servicioPrecio = servicioPrecio;
        registrarOperacion("INICIALIZACION", "Carrito creado");
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (!producto.isDisponible()) {
            throw new IllegalStateException("El producto no esta disponible: " + producto.getNombre());
        }

        Optional<ItemCarrito> itemExistente = buscarItemPorProducto(producto);

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            int cantidadAnterior = item.getCantidad();
            item.incrementarCantidad(cantidad);
            registrarOperacion(
                "ACTUALIZAR",
                String.format(
                    "Producto '%s' actualizado de %d a %d unidades",
                    producto.getNombre(),
                    cantidadAnterior,
                    item.getCantidad()
                )
            );
        } else {
            ItemCarrito nuevoItem = new ItemCarrito(producto, cantidad);
            items.add(nuevoItem);
            registrarOperacion(
                "AGREGAR",
                String.format("Producto '%s' agregado con cantidad %d", producto.getNombre(), cantidad)
            );
        }
    }

    public void removerProducto(String idProducto) {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede estar vacio");
        }

        ItemCarrito itemRemovido = null;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getProducto().getId().equals(idProducto)) {
                itemRemovido = items.remove(i);
                break;
            }
        }

        if (itemRemovido != null) {
            registrarOperacion(
                "REMOVER",
                String.format("Producto '%s' removido del carrito", itemRemovido.getProducto().getNombre())
            );
        } else {
            throw new IllegalArgumentException("Producto no encontrado en el carrito: " + idProducto);
        }
    }

    public void actualizarCantidad(String idProducto, int nuevaCantidad) {
        if (idProducto == null || idProducto.trim().isEmpty()) {
            throw new IllegalArgumentException("El ID del producto no puede estar vacio");
        }
        if (nuevaCantidad <= 0) {
            throw new IllegalArgumentException("La nueva cantidad debe ser mayor a cero");
        }

        Optional<ItemCarrito> itemOpt = items.stream()
            .filter(item -> item.getProducto().getId().equals(idProducto))
            .findFirst();

        if (itemOpt.isPresent()) {
            ItemCarrito item = itemOpt.get();
            int cantidadAnterior = item.getCantidad();
            item.setCantidad(nuevaCantidad);
            registrarOperacion(
                "ACTUALIZAR",
                String.format(
                    "Cantidad de '%s' actualizada de %d a %d",
                    item.getProducto().getNombre(),
                    cantidadAnterior,
                    nuevaCantidad
                )
            );
        } else {
            throw new IllegalArgumentException("Producto no encontrado en el carrito: " + idProducto);
        }
    }

    public void vaciar() {
        int cantidadItems = items.size();
        items.clear();
        registrarOperacion("VACIAR", String.format("Carrito vaciado. Se removieron %d items", cantidadItems));
    }

    public double calcularSubtotal() {
        return items.stream()
            .mapToDouble(ItemCarrito::getSubtotal)
            .sum();
    }

    public double calcularTotal() {
        double subtotal = calcularSubtotal();
        double descuento = servicioPrecio.calcularDescuento(subtotal);
        double montoConDescuento = subtotal - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(montoConDescuento);
        return montoConDescuento + impuesto;
    }

    public String obtenerResumenCompra() {
        if (items.isEmpty()) {
            return "Carrito vacio";
        }

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESUMEN DE COMPRA ===\n\n");

        for (ItemCarrito item : items) {
            resumen.append(item.toString()).append("\n");
        }

        double subtotal = calcularSubtotal();
        double descuento = servicioPrecio.calcularDescuento(subtotal);
        double montoConDescuento = subtotal - descuento;
        double impuesto = servicioPrecio.calcularImpuesto(montoConDescuento);
        double total = montoConDescuento + impuesto;

        resumen.append("\n");
        resumen.append(String.format("Subtotal:        $%.2f\n", subtotal));
        resumen.append(String.format("Descuento:      -$%.2f\n", descuento));
        resumen.append(String.format("Base imponible:  $%.2f\n", montoConDescuento));
        resumen.append(String.format("Impuesto:       +$%.2f\n", impuesto));
        resumen.append(String.format("TOTAL:           $%.2f\n", total));

        return resumen.toString();
    }

    public boolean contieneProducto(String idProducto) {
        return items.stream()
            .anyMatch(item -> item.getProducto().getId().equals(idProducto));
    }

    public int getCantidadItems() {
        return items.size();
    }

    public int getCantidadTotalProductos() {
        return items.stream()
            .mapToInt(ItemCarrito::getCantidad)
            .sum();
    }

    public List<ItemCarrito> getItems() {
        return new ArrayList<>(items);
    }

    public List<HistorialOperacion> getHistorial() {
        return new ArrayList<>(historial);
    }

    public String obtenerHistorialFormateado() {
        if (historial.isEmpty()) {
            return "No hay operaciones registradas";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== HISTORIAL DE OPERACIONES ===\n\n");
        for (HistorialOperacion operacion : historial) {
            sb.append(operacion).append("\n");
        }
        return sb.toString();
    }

    public Optional<ItemCarrito> obtenerItemConMayorSubtotal() {
        return items.stream()
            .max((a, b) -> Double.compare(a.getSubtotal(), b.getSubtotal()));
    }

    private Optional<ItemCarrito> buscarItemPorProducto(Producto producto) {
        return items.stream()
            .filter(item -> item.getProducto().equals(producto))
            .findFirst();
    }

    private void registrarOperacion(String tipo, String detalle) {
        historial.add(new HistorialOperacion(tipo, detalle));
    }
}
