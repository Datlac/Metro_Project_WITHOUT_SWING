package com.metro.business;

public class Transaction {
    private String transactionId;
    private double amount;
    private String paymentMethod; // Hoặc dùng Enum PaymentMethod
    private boolean isSuccess;

    public Transaction(String transactionId, double amount, String paymentMethod) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.isSuccess = true;
    }

    public void generateInvoice() {
        System.out.println("Invoice [" + transactionId + "]: " + amount + " via " + paymentMethod);
    }
}