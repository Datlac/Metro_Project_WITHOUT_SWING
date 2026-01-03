package com.metro.business;

import com.metro.enums.AccountStatus; // Dùng lại Enum AccountStatus
import java.time.LocalDate;

public class SmartCard {
    private String cardNumber;
    private double balance;
    private LocalDate expiryDate;
    private AccountStatus status;

    public SmartCard(String cardNumber) {
        this.cardNumber = cardNumber;
        this.balance = 0.0;
        this.expiryDate = LocalDate.now().plusYears(2);
        this.status = AccountStatus.ACTIVE;
    }

    public void topUp(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Card " + cardNumber + " topped up: " + amount);
        }
    }

    public boolean pay(double amount) {
        if (balance >= amount && status == AccountStatus.ACTIVE) {
            balance -= amount;
            return true;
        }
        return false;
    }
}