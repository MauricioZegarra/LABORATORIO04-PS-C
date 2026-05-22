package pe.com.lab04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class InventarioGUI extends JFrame {

    private final InventarioService inventarioService;
    private final DefaultTableModel modeloTabla;
    private final JTextArea areaMovimientos;

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;

    private JLabel errorCodigo;
    private JLabel errorNombre;
    private JLabel errorPrecio;
    private JLabel errorCantidad;

    public InventarioGUI() {
        inventarioService = new InventarioService();

        setTitle("MiInventario");
        setSize(1100, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titulo = new JLabel("Gestor de Inventario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 25));
        panelPrincipal.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = crearFormulario();
        JScrollPane scrollFormulario = new JScrollPane(panelFormulario);
        scrollFormulario.setPreferredSize(new Dimension(360, 0));
        panelPrincipal.add(scrollFormulario, BorderLayout.WEST);

        modeloTabla = new DefaultTableModel(
                new Object[]{"Código", "Nombre", "Precio", "Stock unidades", "Valor total"}, 0
        );

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(26);
        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Productos registrados"));
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        areaMovimientos = new JTextArea(9, 30);
        areaMovimientos.setEditable(false);
        areaMovimientos.setFont(new Font("Consolas", Font.PLAIN, 12));
        areaMovimientos.setBorder(BorderFactory.createTitledBorder("Historial de movimientos"));
        panelPrincipal.add(new JScrollPane(areaMovimientos), BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder("Datos del producto"));

        txtCodigo = crearCampo();
        txtNombre = crearCampo();
        txtPrecio = crearCampo();
        txtCantidad = crearCampo();

        errorCodigo = crearLabelError();
        errorNombre = crearLabelError();
        errorPrecio = crearLabelError();
        errorCantidad = crearLabelError();

        panel.add(crearGrupoCampo("Código:", txtCodigo, errorCodigo));
        panel.add(crearAyuda("Ejemplo válido: P001, P002, P120"));

        panel.add(crearGrupoCampo("Nombre:", txtNombre, errorNombre));
        panel.add(crearGrupoCampo("Precio por unidad:", txtPrecio, errorPrecio));
        panel.add(crearGrupoCampo("Cantidad de unidades:", txtCantidad, errorCantidad));

        JButton btnRegistrar = new JButton("Registrar producto");
        JButton btnEntrada = new JButton("Agregar stock");
        JButton btnSalida = new JButton("Extraer stock");
        JButton btnBuscar = new JButton("Buscar producto");
        JButton btnMovimientos = new JButton("Ver movimientos");
        JButton btnImportar = new JButton("Importar CSV");
        JButton btnExportar = new JButton("Exportar CSV");
        JButton btnLimpiar = new JButton("Limpiar campos");

        btnRegistrar.addActionListener(e -> registrarProducto());
        btnEntrada.addActionListener(e -> agregarStock());
        btnSalida.addActionListener(e -> extraerStock());
        btnBuscar.addActionListener(e -> buscarProducto());
        btnMovimientos.addActionListener(e -> mostrarMovimientos());
        btnImportar.addActionListener(e -> importarCSV());
        btnExportar.addActionListener(e -> exportarCSV());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        panel.add(Box.createVerticalStrut(8));
        panel.add(btnRegistrar);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnEntrada);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnSalida);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnBuscar);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnMovimientos);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnImportar);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnExportar);
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnLimpiar);

        return panel;
    }

    private JTextField crearCampo() {
        JTextField campo = new JTextField();
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campo.setFont(new Font("Arial", Font.PLAIN, 13));
        return campo;
    }

    private JLabel crearLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        return label;
    }

    private JLabel crearLabelError() {
        JLabel label = new JLabel(" ");
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setForeground(new Color(190, 0, 0));
        return label;
    }

    private JLabel crearAyuda(String texto) {
        JLabel ayuda = new JLabel(texto);
        ayuda.setFont(new Font("Arial", Font.PLAIN, 11));
        ayuda.setForeground(new Color(90, 90, 90));
        ayuda.setAlignmentX(Component.LEFT_ALIGNMENT);
        return ayuda;
    }

    private JPanel crearGrupoCampo(String texto, JTextField campo, JLabel error) {
        JPanel grupo = new JPanel();
        grupo.setLayout(new BoxLayout(grupo, BoxLayout.Y_AXIS));
        grupo.setBackground(Color.WHITE);
        grupo.setAlignmentX(Component.LEFT_ALIGNMENT);

        grupo.add(crearLabel(texto));
        grupo.add(campo);
        grupo.add(error);
        grupo.add(Box.createVerticalStrut(5));

        return grupo;
    }

    private void registrarProducto() {
        limpiarErrores();

        try {
            validarFormularioProducto();

            String codigo = txtCodigo.getText().trim().toUpperCase();
            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            Producto producto = new Producto(codigo, nombre, precio, cantidad);

            inventarioService.registrarProducto(producto);

            actualizarTabla();
            mostrarMovimientosDelProducto(producto);
            mostrarMensaje("Producto registrado correctamente.\nStock registrado: " + cantidad + " unidades.");
            limpiarCampos();

        } catch (InventarioException e) {
            mostrarErrorGeneral(e.getMessage());
        }
    }

    private void agregarStock() {
        limpiarErrores();

        try {
            validarCodigoYCantidad();

            String codigo = txtCodigo.getText().trim().toUpperCase();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            inventarioService.agregarStock(codigo, cantidad);

            Producto producto = inventarioService.obtenerProducto(codigo);

            actualizarTabla();
            mostrarMovimientosDelProducto(producto);
            mostrarMensaje("Stock agregado correctamente.\nSe agregaron " + cantidad + " unidades.");
            limpiarCampos();

        } catch (InventarioException e) {
            mostrarErrorGeneral(e.getMessage());
        }
    }

    private void extraerStock() {
        limpiarErrores();

        try {
            validarCodigoYCantidad();

            String codigo = txtCodigo.getText().trim().toUpperCase();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            inventarioService.extraerStock(codigo, cantidad);

            Producto producto = inventarioService.obtenerProducto(codigo);

            actualizarTabla();
            mostrarMovimientosDelProducto(producto);
            mostrarMensaje("Stock extraído correctamente.\nSe retiraron " + cantidad + " unidades.");
            limpiarCampos();

        } catch (InventarioException e) {
            mostrarErrorGeneral(e.getMessage());
        }
    }

    private void buscarProducto() {
        limpiarErrores();

        try {
            validarSoloCodigo();

            Producto producto = inventarioService.obtenerProducto(txtCodigo.getText().trim().toUpperCase());

            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(String.valueOf(producto.getPrecio()));
            txtCantidad.setText(String.valueOf(producto.getCantidad()));

            mostrarMovimientosDelProducto(producto);
            mostrarMensaje("Producto encontrado.");

        } catch (InventarioException e) {
            mostrarErrorGeneral(e.getMessage());
        }
    }

    private void mostrarMovimientos() {
        limpiarErrores();

        try {
            validarSoloCodigo();

            Producto producto = inventarioService.obtenerProducto(txtCodigo.getText().trim().toUpperCase());
            mostrarMovimientosDelProducto(producto);

        } catch (InventarioException e) {
            mostrarErrorGeneral(e.getMessage());
        }
    }

    private void importarCSV() {
        JFileChooser chooser = new JFileChooser();
        int seleccion = chooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                ResultadoImportacion resultado = ImportadorExportadorCSV.importarProductos(chooser.getSelectedFile());

                int registrados = 0;

                for (Producto producto : resultado.getProductosValidos()) {
                    try {
                        inventarioService.registrarProducto(producto);
                        registrados++;
                    } catch (InventarioException e) {
                        System.out.println("ERROR DE IMPORTACIÓN | Código: " + producto.getCodigo() + " | " + e.getMessage());
                    }
                }

                actualizarTabla();

                System.out.println();
                System.out.println("========== RESULTADO DE IMPORTACIÓN ==========");
                System.out.println("Productos válidos leídos: " + resultado.getProductosValidos().size());
                System.out.println("Productos registrados: " + registrados);
                System.out.println("Errores encontrados: " + resultado.getErrores().size());

                for (ErrorImportacion error : resultado.getErrores()) {
                    System.out.println(error);
                }

                System.out.println("==============================================");
                System.out.println();

                mostrarMensaje("Importación finalizada.\nProductos registrados: " + registrados);

            } catch (InventarioException e) {
                mostrarErrorGeneral(e.getMessage());
            }
        }
    }

    private void exportarCSV() {
        JFileChooser chooser = new JFileChooser();
        int seleccion = chooser.showSaveDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                ImportadorExportadorCSV.exportarProductos(
                        chooser.getSelectedFile(),
                        inventarioService.listarProductos()
                );

                mostrarMensaje("Inventario exportado correctamente.");

            } catch (InventarioException e) {
                mostrarErrorGeneral(e.getMessage());
            }
        }
    }

    private void mostrarMovimientosDelProducto(Producto producto) {
        areaMovimientos.setText("");
        areaMovimientos.append("Historial del producto: " + producto.getCodigo() + " - " + producto.getNombre() + "\n");
        areaMovimientos.append("Stock actual: " + producto.getCantidad() + " unidades\n");
        areaMovimientos.append("------------------------------------------------------\n");

        for (Movimiento movimiento : producto.getMovimientos()) {
            areaMovimientos.append(movimiento.toString() + "\n");
        }
    }

    private void validarFormularioProducto() {
        boolean hayError = false;

        String codigo = txtCodigo.getText().trim().toUpperCase();
        String nombre = txtNombre.getText().trim();

        if (!codigo.matches("^P\\d{3}$")) {
            errorCodigo.setText("Formato inválido. Usa P001 a P999.");
            hayError = true;
        }

        if (nombre.isEmpty()) {
            errorNombre.setText("El nombre no puede estar vacío.");
            hayError = true;
        } else if (nombre.length() < 2 || nombre.length() > 40) {
            errorNombre.setText("Debe tener entre 2 y 40 caracteres.");
            hayError = true;
        } else if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ0-9 ]+$")) {
            errorNombre.setText("Solo letras, números y espacios.");
            hayError = true;
        }

        if (!validarDoubleEnRango(txtPrecio.getText(), 0.01, 10000)) {
            errorPrecio.setText("Precio válido: S/ 0.01 a S/ 10000.");
            hayError = true;
        }

        if (!validarEnteroEnRango(txtCantidad.getText(), 0, 10000)) {
            errorCantidad.setText("Stock inicial válido: 0 a 10000 unidades.");
            hayError = true;
        }

        if (hayError) {
            throw new InventarioException("Corrige los campos marcados en rojo.");
        }
    }

    private void validarCodigoYCantidad() {
        boolean hayError = false;

        String codigo = txtCodigo.getText().trim().toUpperCase();

        if (!codigo.matches("^P\\d{3}$")) {
            errorCodigo.setText("Formato inválido. Usa P001 a P999.");
            hayError = true;
        }

        if (!validarEnteroEnRango(txtCantidad.getText(), 1, 5000)) {
            errorCantidad.setText("Cantidad válida por movimiento: 1 a 5000 unidades.");
            hayError = true;
        }

        if (hayError) {
            throw new InventarioException("Corrige los campos marcados en rojo.");
        }
    }

    private void validarSoloCodigo() {
        if (!txtCodigo.getText().trim().toUpperCase().matches("^P\\d{3}$")) {
            errorCodigo.setText("Formato inválido. Ejemplo: P001.");
            throw new InventarioException("Corrige el código del producto.");
        }
    }

    private boolean validarEnteroEnRango(String texto, int minimo, int maximo) {
        try {
            int valor = Integer.parseInt(texto.trim());
            return valor >= minimo && valor <= maximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validarDoubleEnRango(String texto, double minimo, double maximo) {
        try {
            double valor = Double.parseDouble(texto.trim());
            return valor >= minimo && valor <= maximo;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void limpiarErrores() {
        errorCodigo.setText(" ");
        errorNombre.setText(" ");
        errorPrecio.setText(" ");
        errorCantidad.setText(" ");
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        limpiarErrores();
    }

    private void mostrarErrorGeneral(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Validación del sistema", JOptionPane.WARNING_MESSAGE);
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);

        for (Producto producto : inventarioService.listarProductos()) {
            modeloTabla.addRow(new Object[]{
                producto.getCodigo(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.obtenerValorTotal()
            });
        }
    }
}
