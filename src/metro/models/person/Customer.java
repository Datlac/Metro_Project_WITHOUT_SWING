package metro.models.person;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import metro.enums.CustomerType;
import metro.models.finance.Ticket;

public class Customer extends Person {
    private String customerId;
    private double walletBalance;
    private List<Ticket> ticketHistory;
    private CustomerType type;

    public Customer(String fullName, String idNumber, LocalDate dob, String phoneNumber, String customerId, CustomerType type) {
        super(fullName, idNumber, dob, phoneNumber);
        this.customerId = customerId;
        this.type = type;
        this.walletBalance = 0.0;
        this.ticketHistory = new ArrayList<>();
    }

    public boolean topUpBalance(double amount) {
        if (amount > 0) {
            this.walletBalance += amount;
            System.out.println("Nạp thành công " + amount + ". Số dư mới: " + walletBalance);
            return true;
        }
        return false;
    }

    public boolean deductBalance(double amount) {
        if (walletBalance >= amount) {
            walletBalance -= amount;
            return true;
        }
        System.out.println("Số dư không đủ!");
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
    public double getWalletBalance() { return walletBalance; }
}