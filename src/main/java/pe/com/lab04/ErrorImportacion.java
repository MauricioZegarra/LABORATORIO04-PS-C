package pe.com.lab04;

public class ErrorImportacion {
    private final int fila;
    private final String columna;
    private final String mensaje;

    public ErrorImportacion(int fila, String columna, String mensaje) {
        this.fila = fila;
        this.columna = columna;
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Fila " + fila + " | Columna: " + columna + " | Error: " + mensaje;
    }
}