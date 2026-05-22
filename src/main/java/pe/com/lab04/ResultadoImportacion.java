package pe.com.lab04;

import java.util.ArrayList;
import java.util.List;

public class ResultadoImportacion {
    private final List<Producto> productosValidos = new ArrayList<>();
    private final List<ErrorImportacion> errores = new ArrayList<>();

    public void agregarProductoValido(Producto producto) {
        productosValidos.add(producto);
    }

    public void agregarError(ErrorImportacion error) {
        errores.add(error);
    }

    public List<Producto> getProductosValidos() {
        return productosValidos;
    }

    public List<ErrorImportacion> getErrores() {
        return errores;
    }

    public boolean tieneErrores() {
        return !errores.isEmpty();
    }
}