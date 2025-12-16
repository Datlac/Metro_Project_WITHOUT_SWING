package metro.models.finance;
import java.time.LocalDate;

import metro.enums.TicketStatus;
import metro.enums.TicketType;
import metro.models.person.Customer;

public class Ticket {
    private String ticketId;
    private Customer customer;
    private TicketType ticketType;
    private TicketStatus status;
    private double price;
    private String entryStationId;
    private String exitStationId;
    private LocalDate issueDate;
    private LocalDate expiryDate;

    public Ticket(String ticketId, Customer customer, TicketType ticketType, double price) {
        this.ticketId = ticketId;
        this.customer = customer;
        this.ticketType = ticketType;
        this.price = price;
        this.status = TicketStatus.ACTIVE;
        this.issueDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusDays(1);
    }

    public boolean isValid() {
        return status == TicketStatus.ACTIVE && LocalDate.now().isBefore(expiryDate);
    }

    public void setJourney(String entryId, String exitId) {
        this.entryStationId = entryId;
        this.exitStationId = exitId;
    }

    public double getPrice() { return price; }
    public TicketType getTicketType() { return ticketType; }
    public String getTicketId() { return ticketId; }

    @Override
    public String toString() {
        return String.format("Ticket[%s] - Type: %s - Price: %.0f", ticketId, ticketType, price);
    }
}