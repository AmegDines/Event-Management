package com.eventmanager;

import com.eventmanager.database.DatabaseConnection;
import com.eventmanager.view.MainFrame;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Initialize Database
        DatabaseConnection.initializeDatabase();
        
        // Setup modern Look and Feel (FlatLaf)
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        // Launch Application
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        });
    }
}
