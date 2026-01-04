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
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void linkAccount(Account account) {
        this.account = account;
    }
}