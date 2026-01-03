package com.metro.app;

import com.metro.enums.CustomerType;
import com.metro.enums.StationStatus;
import com.metro.enums.TicketType;
import com.metro.people.Customer;
import com.metro.people.Driver;
import com.metro.infrastructure.Station;
import com.metro.infrastructure.Line;
import com.metro.infrastructure.Route;
import com.metro.infrastructure.AutoGate;
import com.metro.business.Ticket;
import com.metro.business.RevenueManager;
import com.metro.transport.Train;
import com.metro.transport.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== METRO SYSTEM (JAVA 8) - FIXED ===\n");

        // 1. Khởi tạo Cơ sở hạ tầng (Infrastructure)
        Station s1 = new Station("S01", "Ben Thanh");
        Station s2 = new Station("S02", "Ba Son");
        
        // FIX: Line cần 2 tham số (Mã, Tên)
        Line line1 = new Line("L01", "Metro Line 1"); 
        line1.addStation(s1);
        line1.addStation(s2);

        // FIX: AutoGate cần SerialNumber (do kế thừa Device)
        AutoGate gate = new AutoGate("G01", "GATE-SN-999"); 

        // 2. Khởi tạo Con người (People)
        // FIX: Driver cần đầy đủ thông tin Staff (ID, Dept, JobTitle, License, ExpireDate, Hours)
        Driver driver = new Driver(
            "Nguyen Van Lai", "079090", LocalDate.of(1985, 1, 1), "0909000111", 
            "DRV01", "Operations"
        );

        // FIX: Customer cần thêm Membership Level (nếu dùng class đầy đủ)
        Customer customer = new Customer(
            "Tran Van Khach", "079080", LocalDate.of(2000, 5, 5), "0908000222", 
            "C01", 100.0, CustomerType.ADULT
        );

        // 3. Quy trình mua vé (Business)
        Ticket t1 = new Ticket("T01", 15.0, TicketType.SINGLERIDE);
        Ticket t2 = new Ticket("T02", 20.0, TicketType.SINGLERIDE);

        if(customer.deductBalance(t1.getPrice())) {
            customer.addTicket(t1);
            System.out.println("Customer bought ticket T01");
        }

        // 4. Sử dụng cổng (Gate)
        gate.scanTicket(t1); // Vé hợp lệ -> Cổng mở
        gate.scanTicket(t1); // Vé đã dùng -> Cổng đóng

        // 5. Vận hành tàu (Transport)
        Train train = new Train("TR01", 200);
        
        // FIX: Tạo Route trước khi tạo Trip
        List<Station> stops = new ArrayList<>();
        stops.add(s1);
        stops.add(s2);
        Route route = new Route("R01", "Ben Thanh -> Suoi Tien", s1, s2, stops);

        // FIX: Trip cần Route và Thời gian khởi hành
        Trip trip = new Trip("TRIP01", train, driver, route, LocalDateTime.now().plusMinutes(15));
        
        trip.start();

        // 6. Báo cáo doanh thu
        List<Ticket> soldTickets = new ArrayList<>();
        soldTickets.add(t1);
        soldTickets.add(t2);

        RevenueManager finance = new RevenueManager();
        finance.printRevenueReport(soldTickets);
        
        // In lịch sử khách hàng
        // Lưu ý: Đảm bảo class Customer có phương thức printHistory() hoặc dùng getter
        System.out.println("Customer balance: " + customer.getCustomerId());
    }
}