package com.metro.app;

import com.metro.enums.CustomerType;
import com.metro.enums.TicketType;
import com.metro.people.Customer;
import com.metro.people.Driver;
import com.metro.infrastructure.Station;
import com.metro.infrastructure.Line;
import com.metro.infrastructure.AutoGate;
import com.metro.business.Ticket;
import com.metro.business.RevenueManager;
import com.metro.transport.Train;
import com.metro.transport.Trip;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== METRO SYSTEM (JAVA 8) ===\n");

        // 1. Khởi tạo Cơ sở hạ tầng
        Station s1 = new Station("S01", "Ben Thanh");
        Station s2 = new Station("S02", "Ba Son");
        Line line1 = new Line("L01");
        line1.addStation(s1);
        line1.addStation(s2);
        AutoGate gate = new AutoGate("G01");

        // 2. Khởi tạo Con người
        Driver driver = new Driver("Nguyen Van Lai", "079090", LocalDate.of(1985, 1, 1), "0909", "DRV01", "B2");
        Customer customer = new Customer("Tran Van Khach", "079080", LocalDate.of(2000, 5, 5), "0908", "C01", 100.0, CustomerType.ADULT);

        // 3. Quy trình mua vé
        Ticket t1 = new Ticket("T01", 15.0, TicketType.SINGLERIDE);
        Ticket t2 = new Ticket("T02", 20.0, TicketType.SINGLERIDE);

        if(customer.deductBalance(t1.getPrice())) {
            customer.addTicket(t1);
            System.out.println("Customer bought ticket T01");
        }

        // 4. Sử dụng cổng (Gate)
        gate.scanTicket(t1); // Vé hợp lệ -> Cổng mở
        gate.scanTicket(t1); // Vé đã dùng -> Cổng đóng

        // 5. Vận hành tàu (Trip)
        Train train = new Train("TR01", 200);
        Trip trip = new Trip("TRIP01", train, driver);
        trip.start();

        // 6. Báo cáo doanh thu (Sử dụng Stream API trong RevenueManager)
        List<Ticket> soldTickets = new ArrayList<>();
        soldTickets.add(t1);
        soldTickets.add(t2);

        RevenueManager finance = new RevenueManager();
        finance.printRevenueReport(soldTickets);
        
        // 7. In lịch sử vé của khách (Lambda)
        customer.printHistory();
    }
}