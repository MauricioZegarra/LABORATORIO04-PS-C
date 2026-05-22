package pe.com.lab04;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ImportadorExportadorCSV {

    public static ResultadoImportacion importarProductos(File archivo) {
        ResultadoImportacion resultado = new ResultadoImportacion();

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            int fila = 0;

            while ((linea = br.readLine()) != null) {
                fila++;

                if (fila == 1 && linea.toLowerCase().contains("codigo")) {
                    continue;
                }

                String[] columnas = linea.split(",");

                if (columnas.length != 4) {
                    resultado.agregarError(new ErrorImportacion(fila, "estructura", "La fila debe tener 4 columnas."));
                    continue;
                }

                String codigo = columnas[0].trim();
                String nombre = columnas[1].trim();
                String precioTexto = columnas[2].trim();
                String cantidadTexto = columnas[3].trim();

                try {
                    if (codigo.isEmpty()) {
                        throw new InventarioException("El código no puede estar vacío.");
                    }

                    if (nombre.isEmpty()) {
                        throw new InventarioException("El nombre no puede estar vacío.");
                    }

                    double precio;
                    try {
                        precio = Double.parseDouble(precioTexto);
                    } catch (NumberFormatException e) {
                        resultado.agregarError(new ErrorImportacion(fila, "precio", "Precio inválido: " + precioTexto));
                        continue;
                    }

                    int cantidad;
                    try {
                        cantidad = Integer.parseInt(cantidadTexto);
                    } catch (NumberFormatException e) {
                        resultado.agregarError(new ErrorImportacion(fila, "cantidad", "Cantidad inválida: " + cantidadTexto));
                        continue;
                    }

                    Producto producto = new Producto(codigo, nombre, precio, cantidad);
                    resultado.agregarProductoValido(producto);

                } catch (InventarioException e) {
                    resultado.agregarError(new ErrorImportacion(fila, "validación", e.getMessage()));
                }
            }

        } catch (IOException e) {
            throw new InventarioException("No se pudo leer el archivo CSV: " + e.getMessage());
        }

        return resultado;
    }

    public static void exportarProductos(File archivo, List<Producto> productos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("codigo,nombre,precio,cantidad,valor_total");

            for (Producto p : productos) {
                pw.println(p.getCodigo() + "," +
                        p.getNombre() + "," +
                        p.getPrecio() + "," +
                        p.getCantidad() + "," +
                        p.obtenerValorTotal());
            }

        } catch (IOException e) {
            throw new InventarioException("No se pudo exportar el archivo CSV: " + e.getMessage());
        }
    }
}