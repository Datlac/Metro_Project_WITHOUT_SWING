package com.metro.business;

import com.metro.enums.OrderStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private String customerId;
    private LocalDate createdDate;
    private OrderStatus status;
    private List<Ticket> tickets;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.createdDate = LocalDate.now();
        this.status = OrderStatus.PENDING;
        this.tickets = new ArrayList<>();
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
    }

    public double calculateTotal() {
        return tickets.stream().mapToDouble(Ticket::getPrice).sum();
    }
    
    public String getOrderId() { return orderId; }
}