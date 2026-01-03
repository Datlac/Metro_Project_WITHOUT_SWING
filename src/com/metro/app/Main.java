package com.metro.app;

import com.metro.business.FareCalculator;
import com.metro.business.PaymentService;
import com.metro.business.Ticket;
import com.metro.enums.TicketType;
import com.metro.infrastructure.Line;
import com.metro.infrastructure.Station;
import com.metro.transport.RoutePlanner;
import com.metro.transport.TrafficControl;
import com.metro.ui.MetroSystemUI;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

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