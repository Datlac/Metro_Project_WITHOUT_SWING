package metro.services;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import metro.models.finance.Ticket;

public class RevenueManager {
    public void printRevenueReport(List<Ticket> soldTickets, boolean ascending) {
        // Use Streams to group by TicketType and sum prices
        Map<String, Double> revenueMap = soldTickets.stream()
            .collect(Collectors.groupingBy(
                t -> t.getTicketType().toString(),
                TreeMap::new, // Keep natural order for keys if desired, or let TreeMap sort it
                Collectors.summingDouble(Ticket::getPrice)
            ));

        System.out.println("\n=== BÁO CÁO DOANH THU (JAVA 8 STREAMS) ===");
        
        Comparator<String> keyComparator = ascending ? Comparator.naturalOrder() : Comparator.reverseOrder();
        
        System.out.println(ascending ? ">> Sắp xếp TĂNG DẦN theo loại vé (Key):" : ">> Sắp xếp GIẢM DẦN theo loại vé (Key):");
        
        revenueMap.keySet().stream()
            .sorted(keyComparator)
            .forEach(key -> System.out.printf("- %-15s: %,10.0f VND%n", key, revenueMap.get(key)));
    }
    
    public void printRevenueByValue(List<Ticket> soldTickets) {
        // Group and sum
        Map<String, Double> revenueMap = soldTickets.stream()
            .collect(Collectors.groupingBy(
                t -> t.getTicketType().toString(),
                Collectors.summingDouble(Ticket::getPrice)
            ));

        System.out.println("\n>> Top doanh thu cao nhất (Sort List by Value):");
        
        revenueMap.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> System.out.printf("- %-15s: %,10.0f VND%n", entry.getKey(), entry.getValue()));
    }
}