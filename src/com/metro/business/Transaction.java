package com.metro.business;

public class Transaction {
	private String transactionId;
	private double amount;
	private String paymentMethod;
	private boolean isSuccess;

	public Transaction(String transactionId, double amount, String paymentMethod) {
		this.transactionId = transactionId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.isSuccess = true;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public void generateInvoice() {
		System.out.println("Invoice [" + transactionId + "]: " + amount + " via " + paymentMethod);
	}
}