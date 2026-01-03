package com.metro.business;

import java.time.LocalDateTime;

public class Invoice {
    private String invoiceId;
    private Order order; // Liên kết với Order
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

    public void printInvoice() {
        System.out.println("=== INVOICE " + invoiceId + " ===");
        System.out.println("Date: " + issuedDate);
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Total: " + totalAmount);
    }
}