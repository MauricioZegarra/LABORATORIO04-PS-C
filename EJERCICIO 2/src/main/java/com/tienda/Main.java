package com.tienda;

import com.tienda.ui.CarritoUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CarritoUI frame = new CarritoUI();
            frame.setVisible(true);
        });
    }
}
