package metro.interfaces;

public interface IPaymentProcessing {
    boolean processPayment(double amount);
    void refund(double amount);
}