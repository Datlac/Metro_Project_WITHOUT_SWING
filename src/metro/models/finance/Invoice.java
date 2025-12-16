package metro.models.finance;

import java.time.LocalDateTime;

public class Invoice {
    private String invoiceId;
    private LocalDateTime issueDate;
    private double totalAmount;
    private String taxCode;
    private String content;

    public Invoice(String invoiceId, double totalAmount, String content) {
        this.invoiceId = invoiceId;
        this.totalAmount = totalAmount;
        this.content = content;
        this.issueDate = LocalDateTime.now();
        this.taxCode = "MST-METRO-001";
    }

    public void printInvoice() {
        System.out.println("--- INVOICE " + invoiceId + " ---");
        System.out.println("Date: " + issueDate);
        System.out.println("Content: " + content);
        System.out.println("Total: " + totalAmount + " VND");
        System.out.println("---------------------------");
    }
}