package pe.com.lab04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InventarioService {
    private final List<Producto> productos;

    public InventarioService() {
        this.productos = new ArrayList<>();
    }

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new InventarioException("El producto no puede ser nulo.");
        }

        if (buscarProducto(producto.getCodigo()).isPresent()) {
            throw new InventarioException("Ya existe un producto con el código: " + producto.getCodigo());
        }

        productos.add(producto);
    }

    public Optional<Producto> buscarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new InventarioException("El código de búsqueda no puede estar vacío.");
        }

        return productos.stream()
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo.trim()))
                .findFirst();
    }

    public Producto obtenerProducto(String codigo) {
        return buscarProducto(codigo)
                .orElseThrow(() -> new InventarioException("No se encontró un producto con el código: " + codigo));
    }

    public void agregarStock(String codigo, int cantidad) {
        obtenerProducto(codigo).agregarStock(cantidad);
    }

    public void extraerStock(String codigo, int cantidad) {
        obtenerProducto(codigo).extraerStock(cantidad);
    }

    public double calcularValorTotalInventario() {
        return productos.stream()
                .mapToDouble(Producto::obtenerValorTotal)
                .sum();
    }

    public List<Producto> listarProductos() {
        return Collections.unmodifiableList(productos);
    }
}