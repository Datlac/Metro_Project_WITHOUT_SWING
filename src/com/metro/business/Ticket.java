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
    
    // --- CÁC THUỘC TÍNH MỞ RỘNG ---
    private String ownerName;           // Tên khách hàng
    private LocalDateTime purchaseTime; // Thời gian mua
    private LocalDateTime expiryDate;   // Hạn sử dụng
    private String paymentMethod;       // Hình thức thanh toán (Tiền mặt/Ví/Thẻ...)

    // --- CONSTRUCTOR ĐẦY ĐỦ (5 THAM SỐ) ĐỂ KHỚP VỚI UI ---
    // Đây là phần bạn đang thiếu gây ra lỗi
    public Ticket(String ticketId, double price, TicketType type, String ownerName, String paymentMethod) {
        this.ticketId = ticketId;
        this.price = price;
        this.type = type;
        this.ownerName = ownerName;
        this.paymentMethod = paymentMethod;
        
        this.status = TicketStatus.ACTIVE;
        this.purchaseTime = LocalDateTime.now(); 
        
        // Tính hạn sử dụng ngay khi tạo vé
        this.expiryDate = calculateExpiryDate(type);
    }

    // Logic tính hạn sử dụng
    private LocalDateTime calculateExpiryDate(TicketType type) {
        switch (type) {
            case SINGLERIDE: 
                return LocalDateTime.now().plusDays(1); // 24h
            case DAYPASS: 
                return LocalDateTime.now().withHour(23).withMinute(59).withSecond(59); // Cuối ngày
            case MONTHLYPASS: 
                return LocalDateTime.now().plusMonths(1); // 1 tháng
            default: 
                return LocalDateTime.now().plusDays(1);
        }
    }

    // Kiểm tra vé có hợp lệ không (Còn hạn & Active)
    public boolean isValid() {
        if (status != TicketStatus.ACTIVE) return false;
        
        if (LocalDateTime.now().isAfter(expiryDate)) {
            this.status = TicketStatus.EXPIRED;
            return false;
        }
        return true;
    }

    // Sử dụng vé (Quẹt thẻ)
    public void useTicket() {
        if (!isValid()) {
            System.out.println("Vé không hợp lệ hoặc đã hết hạn!");
            return;
        }
        // Nếu là vé lượt thì dùng xong đổi trạng thái thành USED
        if (type == TicketType.SINGLERIDE) {
            this.status = TicketStatus.USED;
        }
    }

    // --- GETTERS & SETTERS ---
    public String getTicketId() { return ticketId; }
    public double getPrice() { return price; }
    public TicketType getType() { return type; }
    
    public String getOwnerName() { return ownerName; }       
    public LocalDateTime getPurchaseTime() { return purchaseTime; } 
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public String getPaymentMethod() { return paymentMethod; } // Getter cho phương thức thanh toán
    
    public void setStatus(TicketStatus status) { this.status = status; }

    // Helper: Format giờ đẹp (VD: 04/01/2026 14:30:00)
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return purchaseTime.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %.0f VND - %s", ticketId, type, price, paymentMethod);
    }
}