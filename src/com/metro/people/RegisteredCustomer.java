package com.metro.people;

import com.metro.enums.CustomerType;
import java.time.LocalDate;
import java.util.Date;

public class RegisteredCustomer extends Customer {
    private String username;
    private String passwordHash;
    private Date registrationDate;
    private Account account; // Quan hệ với Account

    public RegisteredCustomer(String fullName, String idNumber, LocalDate dob, String phoneNumber,
                              String customerId, int walletBalance, 
                              String username, String passwordHash) {
        super(fullName, idNumber, dob, phoneNumber, customerId, walletBalance, CustomerType.ADULT);
        this.username = username;
        this.passwordHash = passwordHash;
        this.registrationDate = new Date();
    }
    
    public void linkAccount(Account account) {
        this.account = account;
    }
}