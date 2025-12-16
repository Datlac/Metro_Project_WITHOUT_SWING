package metro.services;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import metro.models.finance.Ticket;

public class RevenueManager {
    public void printRevenueReport(List<Ticket> soldTickets, boolean ascending) {
        TreeMap<String, Double> revenueMap = new TreeMap<>();

        for (Ticket t : soldTickets) {
            String typeName = t.getTicketType().toString();
            revenueMap.put(typeName, revenueMap.getOrDefault(typeName, 0.0) + t.getPrice());
        }

        System.out.println("\n=== BÁO CÁO DOANH THU (TREEMAP) ===");
        
        if (ascending) {
            System.out.println(">> Sắp xếp TĂNG DẦN theo loại vé (Key):");
            for (Map.Entry<String, Double> entry : revenueMap.entrySet()) {
                System.out.printf("- %-15s: %,10.0f VND%n", entry.getKey(), entry.getValue());
            }
        } else {
            System.out.println(">> Sắp xếp GIẢM DẦN theo loại vé (Key):");
            for (Map.Entry<String, Double> entry : revenueMap.descendingMap().entrySet()) {
                System.out.printf("- %-15s: %,10.0f VND%n", entry.getKey(), entry.getValue());
            }
        }
    }
    
    public void printRevenueByValue(List<Ticket> soldTickets) {
        Map<String, Double> map = new HashMap<>();
        for (Ticket t : soldTickets) {
            String key = t.getTicketType().toString();
            map.put(key, map.getOrDefault(key, 0.0) + t.getPrice());
        }

        List<Map.Entry<String, Double>> list = new ArrayList<>(map.entrySet());
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        System.out.println("\n>> Top doanh thu cao nhất (Sort List by Value):");
        for (Map.Entry<String, Double> entry : list) {
            System.out.printf("- %-15s: %,10.0f VND%n", entry.getKey(), entry.getValue());
        }
    }
}