package com.metro.business;

import com.metro.enums.TicketStatus;
import com.metro.enums.TicketType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
	private String ticketId;
	private double price;
	private TicketType type;
	private TicketStatus status;

	private String ownerName;
	private LocalDateTime purchaseTime;
	private LocalDateTime expiryDate;
	private String paymentMethod;

	public Ticket(String ticketId, double price, TicketType type, String ownerName, String paymentMethod) {
		this.ticketId = ticketId;
		this.price = price;
		this.type = type;
		this.ownerName = ownerName;
		this.paymentMethod = paymentMethod;

		this.status = TicketStatus.ACTIVE;
		this.purchaseTime = LocalDateTime.now();

		this.expiryDate = calculateExpiryDate(type);
	}

	private LocalDateTime calculateExpiryDate(TicketType type) {
		switch (type) {
		case SINGLERIDE:
			return LocalDateTime.now().plusDays(1);
		case DAYPASS:
			return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
		case MONTHLYPASS:
			return LocalDateTime.now().plusMonths(1);
		default:
			return LocalDateTime.now().plusDays(1);
		}
	}

	public boolean isValid() {
		if (status != TicketStatus.ACTIVE)
			return false;

		if (LocalDateTime.now().isAfter(expiryDate)) {
			this.status = TicketStatus.EXPIRED;
			return false;
		}
		return true;
	}

	public void useTicket() {
		if (!isValid()) {
			System.out.println("Vé không hợp lệ hoặc đã hết hạn!");
			return;
		}

		if (type == TicketType.SINGLERIDE) {
			this.status = TicketStatus.USED;
		}
	}

	public String getTicketId() {
		return ticketId;
	}

	public double getPrice() {
		return price;
	}

	public TicketType getType() {
		return type;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public LocalDateTime getPurchaseTime() {
		return purchaseTime;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setStatus(TicketStatus status) {
		this.status = status;
	}

	public String getFormattedTime() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return purchaseTime.format(formatter);
	}

	public void setExpiryDate(LocalDateTime date) {
		this.expiryDate = date;
	}

	public TicketStatus getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s - %.0f VND - %s", ticketId, type, price, paymentMethod);
	}
}