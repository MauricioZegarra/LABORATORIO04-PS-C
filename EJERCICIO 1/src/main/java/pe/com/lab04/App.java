package pe.com.lab04;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InventarioGUI ventana = new InventarioGUI();
            ventana.setVisible(true);
        });
    }
}