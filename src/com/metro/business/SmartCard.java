package com.metro.business;

import com.metro.enums.AccountStatus;
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

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public LocalDate getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDate expiryDate) {
		this.expiryDate = expiryDate;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
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