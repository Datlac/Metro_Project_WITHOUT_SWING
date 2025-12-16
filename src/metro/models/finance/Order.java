package metro.models.finance;
import java.time.LocalDate;
import java.util.ArrayList;

import metro.enums.OrderStatus;

public class Order {
    private String orderId;
    private LocalDate createdTime;
    private String customerId;
    private OrderStatus status;
    private ArrayList<Ticket> orderItems;

    public Order(String orderId, String customerId) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.createdTime = LocalDate.now();
        this.status = OrderStatus.PENDING;
        this.orderItems = new ArrayList<>();
    }

    public void addItem(Ticket ticket) {
        orderItems.add(ticket);
    }

    public void recalculateTotal() {
        double total = 0;
        for (Ticket t : orderItems) {
            total += t.getPrice();
        }
        System.out.println("Tổng tiền đơn hàng: " + total);
    }
}