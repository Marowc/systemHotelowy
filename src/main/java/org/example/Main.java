package org.example;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            DesktopApp app = new DesktopApp();
            app.setVisible(true);
        });
    }
}