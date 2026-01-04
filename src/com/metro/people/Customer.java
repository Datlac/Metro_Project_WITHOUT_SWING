package com.metro.people;

import com.metro.business.Ticket;
import com.metro.enums.CustomerType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Customer extends Person {
	private String customerId;
	private double walletBalance;
	private CustomerType type;

	private List<Ticket> ticketHistory;

	public Customer(String fullName, String idNumber, LocalDate dob, String phoneNumber, String customerId,
			double walletBalance, CustomerType type) {
		super(fullName, idNumber, dob, phoneNumber);
		this.customerId = customerId;
		this.walletBalance = walletBalance;
		this.type = type;
		this.ticketHistory = new ArrayList<>();
	}

	public boolean deductBalance(double amount) {
		if (walletBalance >= amount) {
			walletBalance -= amount;
			return true;
		}
		return false;
	}

	public void topUp(double amount) {
		this.walletBalance += amount;
	}

	public void addTicket(Ticket ticket) {
		this.ticketHistory.add(ticket);
	}

	public List<Ticket> getTicketHistory() {
		return this.ticketHistory;
	}

	public double getWalletBalance() {
		return walletBalance;
	}

	public String getCustomerId() {
		return customerId;
	}

	public CustomerType getType() {
		return type;
	}
}