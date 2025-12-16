package metro.models.finance;
import metro.enums.PaymentMethod;

public class Transaction {
    private String transactionId;
    private double amount;
    private PaymentMethod paymentMethod;
    private boolean isSuccess;

    public Transaction(String id, double amount, PaymentMethod method) {
        this.transactionId = id;
        this.amount = amount;
        this.paymentMethod = method;
        this.isSuccess = true;
    }

    public void generateInvoice() {
        System.out.println("Hóa đơn giao dịch: " + transactionId + " - " + amount + " VND");
    }
}