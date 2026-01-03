package com.metro.app;



import javax.swing.SwingUtilities;

import com.metro.ui.MetroSystemUI;

public class Main {
	
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(() -> {
            try {
                // Làm giao diện đẹp hơn (giống Windows/Mac native)
                javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Khởi động UI
            new MetroSystemUI().setVisible(true);
        });
    }
}