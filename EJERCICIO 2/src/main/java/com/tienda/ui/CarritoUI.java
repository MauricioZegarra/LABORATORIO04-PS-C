package com.tienda.ui;

import com.tienda.modelo.CarritoCompra;
import com.tienda.modelo.ItemCarrito;
import com.tienda.modelo.Producto;
import com.tienda.servicio.ServicioPrecioImpl;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarritoUI extends JFrame {

    private final CarritoCompra carrito;
    private final List<Producto> catalogoProductos;
    private List<Producto> catalogoFiltrado;

    private DefaultTableModel modeloCarrito;
    private JTable tablaCarrito;
    private JComboBox<Producto> comboProductos;
    private JTextField txtFiltro;
    private JSpinner spinnerCantidad;

    private JLabel lblSubtotal;
    private JLabel lblDescuento;
    private JLabel lblImpuesto;
    private JLabel lblTotal;
    private JLabel lblItemsDistintos;
    private JLabel lblUnidadesTotales;
    private JLabel lblMayorSubtotal;

    private JTextArea areaResumen;
    private JTextArea areaHistorial;
    private boolean actualizandoTabla;

    public CarritoUI() {
        this.carrito = new CarritoCompra(new ServicioPrecioImpl(0.10, 0.18));
        this.catalogoProductos = inicializarCatalogo();
        this.catalogoFiltrado = new ArrayList<>(catalogoProductos);

        configurarVentana();
        inicializarComponentes();
        configurarEdicionDirectaEnTabla();
        actualizarComboProductos();
        actualizarInterfaz();
    }

    private void configurarVentana() {
        setTitle("Sistema de Carrito de Compras");
        setSize(1120, 760);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12, 12));
    }

    private void inicializarComponentes() {
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JLabel titulo = new JLabel("Carrito de Compras");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel subtitulo = new JLabel("Agregar, editar y revisar compras con historial de operaciones");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelTitulo = new JPanel(new GridLayout(2, 1));
        panelTitulo.add(titulo);
        panelTitulo.add(subtitulo);
        panel.add(panelTitulo, BorderLayout.NORTH);

        JPanel panelAcciones = new JPanel(new GridBagLayout());
        panelAcciones.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Catálogo y acciones"),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0;
        panelAcciones.add(new JLabel("Buscar:"), c);

        txtFiltro = new JTextField(18);
        c.gridx = 1; c.weightx = 1.0;
        panelAcciones.add(txtFiltro, c);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> filtrarCatalogo());
        c.gridx = 2; c.weightx = 0;
        panelAcciones.add(btnFiltrar, c);

        JButton btnMostrarTodo = new JButton("Mostrar todo");
        btnMostrarTodo.addActionListener(e -> limpiarFiltro());
        c.gridx = 3;
        panelAcciones.add(btnMostrarTodo, c);

        c.gridx = 0; c.gridy = 1;
        panelAcciones.add(new JLabel("Producto:"), c);

        comboProductos = new JComboBox<>();
        comboProductos.setPreferredSize(new Dimension(340, 28));
        comboProductos.setRenderer(crearRendererProducto());
        c.gridx = 1; c.gridwidth = 3;
        panelAcciones.add(comboProductos, c);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 2;
        panelAcciones.add(new JLabel("Cantidad:"), c);

        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
        spinnerCantidad.setPreferredSize(new Dimension(90, 28));
        c.gridx = 1;
        panelAcciones.add(spinnerCantidad, c);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregarProducto());
        JButton btnVaciar = new JButton("Vaciar carrito");
        btnVaciar.addActionListener(e -> vaciarCarrito());
        panelBotones.add(btnAgregar);
        panelBotones.add(btnVaciar);

        c.gridx = 2; c.gridwidth = 2;
        panelAcciones.add(panelBotones, c);

        panel.add(panelAcciones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.62);
        splitPane.setBorder(null);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(8, 8));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Productos en el carrito"));

        String[] columnas = {"ID", "Producto", "Precio Unit.", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 3 ? Integer.class : Object.class;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaCarrito.setRowHeight(24);
        panelIzquierdo.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelAccionesTabla = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        JButton btnRemover = new JButton("Remover seleccionado");
        btnRemover.addActionListener(e -> removerProductoSeleccionado());
        JButton btnResumen = new JButton("Ver resumen");
        btnResumen.addActionListener(e -> mostrarResumen());
        panelAccionesTabla.add(btnRemover);
        panelAccionesTabla.add(btnResumen);
        panelIzquierdo.add(panelAccionesTabla, BorderLayout.SOUTH);

        JTabbedPane tabsDerecha = new JTabbedPane();
        tabsDerecha.addTab("Historial", crearPanelHistorial());
        tabsDerecha.addTab("Resumen", crearPanelResumenCompleto());

        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(tabsDerecha);
        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaHistorial.setLineWrap(true);
        areaHistorial.setWrapStyleWord(true);

        panel.add(new JScrollPane(areaHistorial), BorderLayout.CENTER);

        JButton btnActualizarHistorial = new JButton("Actualizar historial");
        btnActualizarHistorial.addActionListener(e -> actualizarHistorial());
        panel.add(btnActualizarHistorial, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelResumenCompleto() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        areaResumen = new JTextArea();
        areaResumen.setEditable(false);
        areaResumen.setFont(new Font("Monospaced", Font.PLAIN, 11));
        areaResumen.setLineWrap(true);
        areaResumen.setWrapStyleWord(true);

        panel.add(new JScrollPane(areaResumen), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JPanel panelResumen = new JPanel(new GridLayout(2, 4, 8, 8));
        panelResumen.setBorder(BorderFactory.createTitledBorder("Resumen rápido"));

        lblSubtotal = crearValorResumen(panelResumen, "Subtotal:");
        lblDescuento = crearValorResumen(panelResumen, "Descuento:");
        lblImpuesto = crearValorResumen(panelResumen, "Impuesto:");
        lblTotal = crearValorResumen(panelResumen, "Total:");

        lblItemsDistintos = crearValorResumen(panelResumen, "Items distintos:");
        lblUnidadesTotales = crearValorResumen(panelResumen, "Unidades totales:");
        lblMayorSubtotal = crearValorResumen(panelResumen, "Mayor subtotal:");
        JLabel espacio = new JLabel("");
        panelResumen.add(espacio);

        panel.add(panelResumen, BorderLayout.CENTER);
        return panel;
    }

    private JLabel crearValorResumen(JPanel panel, String texto) {
        JPanel caja = new JPanel(new BorderLayout(2, 2));
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Arial", Font.PLAIN, 12));
        JLabel valor = new JLabel("$0.00");
        valor.setFont(new Font("Arial", Font.BOLD, 13));
        caja.add(etiqueta, BorderLayout.NORTH);
        caja.add(valor, BorderLayout.CENTER);
        panel.add(caja);
        return valor;
    }

    private void configurarEdicionDirectaEnTabla() {
        modeloCarrito.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (actualizandoTabla) {
                    return;
                }
                if (e.getType() != TableModelEvent.UPDATE || e.getColumn() != 3) {
                    return;
                }

                int fila = e.getFirstRow();
                if (fila < 0 || fila >= carrito.getItems().size()) {
                    return;
                }

                Object valor = modeloCarrito.getValueAt(fila, 3);
                ItemCarrito item = carrito.getItems().get(fila);

                try {
                    int nuevaCantidad;
                    if (valor instanceof Number numero) {
                        nuevaCantidad = numero.intValue();
                    } else {
                        nuevaCantidad = Integer.parseInt(String.valueOf(valor).trim());
                    }

                    if (nuevaCantidad <= 0) {
                        throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
                    }

                    carrito.actualizarCantidad(item.getProducto().getId(), nuevaCantidad);
                    actualizarInterfaz();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                        CarritoUI.this,
                        "No se pudo actualizar la cantidad: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    actualizarInterfaz();
                }
            }
        });
    }

    private void agregarProducto() {
        try {
            Producto producto = (Producto) comboProductos.getSelectedItem();
            if (producto == null) {
                JOptionPane.showMessageDialog(this, "No hay productos para agregar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int cantidad = (Integer) spinnerCantidad.getValue();
            carrito.agregarProducto(producto, cantidad);
            actualizarInterfaz();
            JOptionPane.showMessageDialog(this, "Producto agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerProductoSeleccionado() {
        int filaSeleccionada = tablaCarrito.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un producto.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ItemCarrito item = carrito.getItems().get(filaSeleccionada);
            int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea remover este producto?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                carrito.removerProducto(item.getProducto().getId());
                actualizarInterfaz();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void vaciarCarrito() {
        if (carrito.getCantidadItems() == 0) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Seguro que desea vaciar todo el carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            carrito.vaciar();
            actualizarInterfaz();
        }
    }

    private void mostrarResumen() {
        JTextArea textArea = new JTextArea(carrito.obtenerResumenCompra(), 18, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(textArea),
            "Resumen de compra",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void actualizarInterfaz() {
        actualizandoTabla = true;
        modeloCarrito.setRowCount(0);

        for (ItemCarrito item : carrito.getItems()) {
            Object[] fila = {
                item.getProducto().getId(),
                item.getProducto().getNombre(),
                String.format("$%.2f", item.getProducto().getPrecio()),
                item.getCantidad(),
                String.format("$%.2f", item.getSubtotal())
            };
            modeloCarrito.addRow(fila);
        }

        actualizandoTabla = false;

        double subtotal = carrito.calcularSubtotal();
        double descuento = subtotal * 0.10;
        double impuesto = (subtotal - descuento) * 0.18;
        double total = carrito.calcularTotal();

        lblSubtotal.setText(String.format("$%.2f", subtotal));
        lblDescuento.setText(String.format("-$%.2f", descuento));
        lblImpuesto.setText(String.format("+$%.2f", impuesto));
        lblTotal.setText(String.format("$%.2f", total));
        lblItemsDistintos.setText(String.valueOf(carrito.getCantidadItems()));
        lblUnidadesTotales.setText(String.valueOf(carrito.getCantidadTotalProductos()));

        Optional<ItemCarrito> mayor = carrito.obtenerItemConMayorSubtotal();
        lblMayorSubtotal.setText(mayor
            .map(item -> item.getProducto().getNombre() + " ($" + String.format("%.2f", item.getSubtotal()) + ")")
            .orElse("Ninguno"));

        areaResumen.setText(carrito.obtenerResumenCompra());
        areaResumen.setCaretPosition(0);
        actualizarHistorial();
    }

    private void actualizarHistorial() {
        areaHistorial.setText(carrito.obtenerHistorialFormateado());
        areaHistorial.setCaretPosition(0);
    }

    private void filtrarCatalogo() {
        String filtro = txtFiltro.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            catalogoFiltrado = new ArrayList<>(catalogoProductos);
        } else {
            catalogoFiltrado = new ArrayList<>();
            for (Producto producto : catalogoProductos) {
                String texto = (producto.getId() + " " + producto.getNombre()).toLowerCase();
                if (texto.contains(filtro)) {
                    catalogoFiltrado.add(producto);
                }
            }
        }

        actualizarComboProductos();
    }

    private void limpiarFiltro() {
        txtFiltro.setText("");
        catalogoFiltrado = new ArrayList<>(catalogoProductos);
        actualizarComboProductos();
    }

    private void actualizarComboProductos() {
        DefaultComboBoxModel<Producto> modelo = new DefaultComboBoxModel<>();
        for (Producto producto : catalogoFiltrado) {
            modelo.addElement(producto);
        }
        comboProductos.setModel(modelo);

        if (modelo.getSize() > 0) {
            comboProductos.setSelectedIndex(0);
        }
    }

    private List<Producto> inicializarCatalogo() {
        List<Producto> catalogo = new ArrayList<>();
        catalogo.add(new Producto("P001", "Laptop HP", 1200.00, true));
        catalogo.add(new Producto("P002", "Mouse Logitech", 25.50, true));
        catalogo.add(new Producto("P003", "Teclado Mecánico", 85.00, true));
        catalogo.add(new Producto("P004", "Monitor 24 pulgadas", 180.00, true));
        catalogo.add(new Producto("P005", "Webcam HD", 45.00, true));
        catalogo.add(new Producto("P006", "Audífonos Bluetooth", 60.00, true));
        catalogo.add(new Producto("P007", "SSD 1TB", 95.00, true));
        catalogo.add(new Producto("P008", "RAM 16GB", 75.00, false));
        catalogo.add(new Producto("P009", "Impresora Multifuncional", 250.00, true));
        catalogo.add(new Producto("P010", "Cable HDMI", 12.50, true));
        return catalogo;
    }

    private ListCellRenderer<? super Producto> crearRendererProducto() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                JList<?> list,
                Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus
            ) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Producto producto) {
                    String texto = String.format("%s - $%.2f %s",
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.isDisponible() ? "" : "(No disponible)");
                    setText(texto);
                }
                return this;
            }
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarritoUI frame = new CarritoUI();
            frame.setVisible(true);
        });
    }
}
