package com.metro.business;

import java.util.List;

public class RevenueManager {
    
    public void printRevenueReport(List<Ticket> soldTickets) {
        double totalRevenue = soldTickets.stream()
                                         .mapToDouble(Ticket::getPrice)
                                         .sum();
        
        long totalTickets = soldTickets.size();

        System.out.println("=== REVENUE REPORT ===");
        System.out.println("Total Tickets Sold: " + totalTickets);
        System.out.println("Total Revenue: " + totalRevenue);
    }
}