package pe.com.lab04;

public class InventarioException extends RuntimeException {
    public InventarioException(String mensaje) {
        super(mensaje);
    }
}