package metro.services;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import metro.enums.TicketType;
import metro.models.finance.Ticket;
import metro.models.person.Customer;

public class MetroSystem {
    private List<Ticket> soldTickets;

    public MetroSystem() {
        this.soldTickets = new ArrayList<>();
    }

    public List<Ticket> getSoldTickets() {
        return soldTickets;
    }
    
    // Thêm vé vào danh sách quản lý
    public void recordTicket(Ticket t) {
        soldTickets.add(t);
    }

    // Hàm đọc file: ticketId, customerId, type, price
    public void importTicketsFromFile(String filePath, List<Customer> customers) {
        System.out.println("\n--- ĐANG ĐỌC DỮ LIỆU TỪ FILE: " + filePath + " ---");
        
        // Optimization: Convert List to Map for O(1) lookup
        Map<String, Customer> customerMap = customers.stream()
                .collect(Collectors.toMap(Customer::getCustomerId, Function.identity()));
        
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            long count = lines
                .filter(line -> !line.trim().isEmpty() && !line.startsWith("#"))
                .map(line -> line.split(","))
                .filter(parts -> parts.length == 4)
                .map(parts -> {
                    String tId = parts[0].trim();
                    String cId = parts[1].trim();
                    String typeStr = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());
                    
                    Customer cust = customerMap.get(cId);
                    if (cust != null) {
                        try {
                            TicketType type = TicketType.valueOf(typeStr);
                            Ticket t = new Ticket(tId, cust, type, price);
                            
                            // Deduct balance for consistency. 
                            // If balance is insufficient, we still record but maybe warn?
                            // For this task, we assume we just deduct to satisfy the formula 
                            // "Balance = Initial - Spent". 
                            // Note: deductBalance returns false if not enough money, but we might want to force it 
                            // or just accept that balance calculation requires it.
                            boolean paid = cust.deductBalance(price);
                            if (!paid) {
                                System.out.println("Warning: Imported ticket " + tId + " for " + cId + " but insufficient funds. Skipping.");
                            } else {
                                this.soldTickets.add(t);
                                cust.addTicket(t);
                            }
                            return paid ? 1 : 0; 
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Lỗi loại vé không tồn tại: " + typeStr);
                        }
                    } else {
                        System.out.println("Không tìm thấy khách hàng mã: " + cId);
                    }
                    return 0;
                })
                .mapToInt(Integer::intValue)
                .sum();

            System.out.println("=> Đã nhập thành công " + count + " vé từ file.");
            
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
        }
    }
}