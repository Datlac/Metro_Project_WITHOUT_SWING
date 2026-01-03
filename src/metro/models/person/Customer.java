package metro.models.person;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import metro.enums.CustomerType;
import metro.models.finance.Ticket;

public class Customer extends Person {
    private String customerId;
    private metro.models.finance.SmartCard card;
    private List<Ticket> ticketHistory; // Interface List
    private CustomerType type;

    public Customer(String fullName, String idNumber, LocalDate dob, String phoneNumber, String customerId, CustomerType type) {
        super(fullName, idNumber, dob, phoneNumber);
        this.customerId = customerId;
        this.type = type;
        // Initialize SmartCard
        this.card = new metro.models.finance.SmartCard(customerId, type); 
        this.ticketHistory = new ArrayList<>();
    }

    public boolean topUpBalance(double amount) {
        return this.card.deposit(amount);
    }

    public boolean deductBalance(double amount) {
        if (this.card.pay(amount)) {
            return true;
        }
        System.out.println("MB: Số dư không đủ! (Cần: " + amount + ", Có: " + card.getBalance() + ")");
        return false;
    }

    public void buyTicket(Ticket ticket) {
        if (deductBalance(ticket.getPrice())) {
            addTicket(ticket);
            System.out.println("Mua vé thành công: " + ticket.toString());
        } else {
            System.out.println("Mua vé thất bại do không đủ tiền.");
        }
    }

    public void addTicket(Ticket ticket) {
        this.ticketHistory.add(ticket);
    }

    public String getCustomerId() { return customerId; }
    
    public double getWalletBalance() { return card.getBalance(); }
    
    public metro.models.finance.SmartCard getSmartCard() {
        return this.card;
    }
    
    public double getTotalSpent() {
        return ticketHistory.stream().mapToDouble(Ticket::getPrice).sum();
    }
    
    public List<Ticket> getTicketHistory() { return ticketHistory; }
}