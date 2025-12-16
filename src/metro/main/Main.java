package metro.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import metro.enums.CustomerType;
import metro.enums.TicketType;
import metro.models.finance.Ticket;
import metro.models.person.Customer;
import metro.models.person.Driver;
import metro.models.transport.Line;
import metro.models.transport.Route;
import metro.models.transport.Station;
import metro.models.transport.Train;
import metro.models.transport.Trip;
import metro.services.MetroSystem;
import metro.services.RevenueManager;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== HỆ THỐNG QUẢN LÝ METRO ===");

        // --- 1. KHỞI TẠO HỆ THỐNG (QUAN TRỌNG: Phải khai báo dòng này trước tiên) ---
        MetroSystem system = new MetroSystem(); 

        // --- 2. Khởi tạo Line & Station ---
        Line line1 = new Line("L01", "Bến Thành - Suối Tiên");
        Station s1 = new Station("S01", "Bến Thành");
        Station s2 = new Station("S02", "Nhà Hát TP");
        Station s3 = new Station("S03", "Ba Son");
        
        line1.addStation(s1);
        line1.addStation(s2);
        line1.addStation(s3);
        
        // Test TreeMap trong Line (Route)
        line1.addRoute("R02", new Route("R02", 5.0));
        line1.addRoute("R01", new Route("R01", 2.5)); 
        line1.showRoutes(); 

        // --- 3. Tạo danh sách khách hàng (Tự động tạo 10 khách C01 -> C10) ---
        // Để phục vụ việc đọc file data_large.txt
        List<Customer> customers = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            String id = String.format("C%02d", i); // Tạo chuỗi C01, C02...
            CustomerType type = (i % 2 == 0) ? CustomerType.ADULT : CustomerType.STUDENT;
            
            Customer c = new Customer(
                "Khách Hàng " + i, 
                "ID_" + id, 
                LocalDate.of(2000, 1, 1), 
                "0909000" + i, 
                id, 
                type
            );
            c.topUpBalance(5000000); // Nạp tiền sẵn để test mua nhiều vé
            customers.add(c);
        }
        System.out.println("Đã tạo " + customers.size() + " khách hàng giả lập.");

        // --- 4. Tạo các thành phần Vận hành (Tàu, Tài xế, Chuyến đi) ---
        Route route1 = new Route("R01", 15.5);
        Train train1 = new Train("TR01", 200);
        Driver driver1 = new Driver("Trần Văn Tài", "07988", LocalDate.of(1980, 1, 1), "0912", 
                                    "NV01", "Vận hành", "Lái tàu", "B2-METRO", LocalDate.of(2030, 1, 1));
        
        Trip trip1 = new Trip(
            "TRIP_001", 
            route1, 
            train1, 
            driver1, 
            LocalDateTime.of(2023, 12, 20, 8, 0), 
            LocalDateTime.of(2023, 12, 20, 9, 0)
        );
        
        System.out.println("\n--- THÔNG TIN CHUYẾN ĐI ---");
        System.out.println(trip1.toString());
        trip1.startTrip();
        trip1.completeTrip();
        
        // --- 5. Test Mua vé Thủ công ---
        System.out.println("\n--- TEST ĐẶT VÉ THỦ CÔNG ---");
        Customer c1 = customers.get(0); // Lấy khách C01
        
        Ticket t1 = new Ticket("TKT_MANUAL_01", c1, TicketType.MONTHLYPASS, 20000);
        c1.buyTicket(t1);
        system.recordTicket(t1); // Sử dụng biến system đã khai báo ở trên

        Ticket t2 = new Ticket("TKT_MANUAL_02", c1, TicketType.SINGLERIDE, 5000);
        c1.buyTicket(t2);
        system.recordTicket(t2);

        // --- 6. Đọc vé từ File (Chức năng đọc file data.txt hoặc data_large.txt) ---
        // Đảm bảo bạn đã tạo file data_large.txt ngang hàng thư mục src
        system.importTicketsFromFile("data_large.txt", customers);

        // --- 7. Thống kê Doanh thu ---
        RevenueManager rm = new RevenueManager();
        
        // Sắp xếp tăng dần theo Loại vé (Key của TreeMap)
        rm.printRevenueReport(system.getSoldTickets(), true);  
        
        // Sắp xếp giảm dần theo Loại vé
        rm.printRevenueReport(system.getSoldTickets(), false); 
        
        // Sắp xếp theo Top doanh thu (Value)
        rm.printRevenueByValue(system.getSoldTickets());       
    }
}