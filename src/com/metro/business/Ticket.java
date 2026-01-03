package com.metro.business;

import com.metro.enums.TicketStatus;
import com.metro.enums.TicketType;
import java.time.LocalDateTime;

public class Ticket {
    private String ticketId;
    private double price;
    private TicketType type;
    private TicketStatus status;
    private LocalDateTime issuedDate;

    public Ticket(String ticketId, double price, TicketType type) {
        this.ticketId = ticketId;
        this.price = price;
        this.type = type;
        this.status = TicketStatus.ACTIVE;
        this.issuedDate = LocalDateTime.now(); 
    }

    public boolean isValid() {
        return status == TicketStatus.ACTIVE;
    }

    public void useTicket() {
        if (type == TicketType.SINGLERIDE) {
            this.status = TicketStatus.USED;
        }
    }

    public double getPrice() { return price; }
    public String getTicketId() { return ticketId; }

    @Override
    public String toString() {
        return "Ticket[ID=" + ticketId + ", Price=" + price + ", Status=" + status + "]";
    }
}