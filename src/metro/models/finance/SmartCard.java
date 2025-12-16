package metro.models.finance;

import metro.enums.CustomerType;
import java.time.LocalDate;

public class SmartCard {
    private String cardNumber;
    private double balance;
    private LocalDate expiryDate;
    private CustomerType linkedType; // Loại thẻ (Sinh viên/Người lớn)
    private boolean isActive;

    public SmartCard(String cardNumber, CustomerType type) {
        this.cardNumber = cardNumber;
        this.linkedType = type;
        this.balance = 0.0;
        this.isActive = true;
        this.expiryDate = LocalDate.now().plusYears(2); // Hạn 2 năm
    }

    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Nạp thẻ " + cardNumber + ": +" + amount);
        }
    }

    public boolean pay(double amount) {
        if (!isActive) return false;
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public double getBalance() { return balance; }
}