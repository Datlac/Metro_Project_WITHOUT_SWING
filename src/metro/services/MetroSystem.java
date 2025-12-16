package metro.services;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;

                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String tId = parts[0].trim();
                    String cId = parts[1].trim();
                    String typeStr = parts[2].trim();
                    double price = Double.parseDouble(parts[3].trim());

                    Customer cust = null;
                    for (Customer c : customers) {
                        if (c.getCustomerId().equals(cId)) {
                            cust = c;
                            break;
                        }
                    }

                    if (cust != null) {
                        try {
                            TicketType type = TicketType.valueOf(typeStr);
                            Ticket t = new Ticket(tId, cust, type, price);
                            this.soldTickets.add(t);
                            cust.addTicket(t); 
                            count++;
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Lỗi loại vé không tồn tại: " + typeStr);
                        }
                    } else {
                        System.out.println("Không tìm thấy khách hàng mã: " + cId);
                    }
                }
            }
            System.out.println("=> Đã nhập thành công " + count + " vé từ file.");
            
        } catch (IOException e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Lỗi dữ liệu: " + e.getMessage());
        }
    }
}