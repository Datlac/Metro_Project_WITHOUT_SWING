package com.metro.business;

import com.metro.enums.OrderStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
	private String orderId;
	private String customerId;
	private LocalDate createdDate;
	private OrderStatus status;
	private List<Ticket> tickets;

	public Order(String orderId, String customerId) {
		this.orderId = orderId;
		this.customerId = customerId;
		this.createdDate = LocalDate.now();
		this.status = OrderStatus.PENDING;
		this.tickets = new ArrayList<>();
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public void addTicket(Ticket t) {
		tickets.add(t);
	}

	public double calculateTotal() {
		return tickets.stream().mapToDouble(Ticket::getPrice).sum();
	}

	public String getOrderId() {
		return orderId;
	}
}