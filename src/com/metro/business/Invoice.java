package com.metro.business;

import java.time.LocalDateTime;

public class Invoice {
	private String invoiceId;
	private Order order;
	private LocalDateTime issuedDate;
	private double totalAmount;
	private String taxCode;

	public Invoice(String invoiceId, Order order, double totalAmount) {
		this.invoiceId = invoiceId;
		this.order = order;
		this.totalAmount = totalAmount;
		this.issuedDate = LocalDateTime.now();
		this.taxCode = "TAX-VN-001";
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public LocalDateTime getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(LocalDateTime issuedDate) {
		this.issuedDate = issuedDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public void printInvoice() {
		System.out.println("=== INVOICE " + invoiceId + " ===");
		System.out.println("Date: " + issuedDate);
		System.out.println("Order ID: " + order.getOrderId());
		System.out.println("Total: " + totalAmount);
	}
}