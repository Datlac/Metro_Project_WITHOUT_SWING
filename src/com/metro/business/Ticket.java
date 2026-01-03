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
    
    // --- THUỘC TÍNH MỚI ---
    private String ownerName;           // Tên khách hàng sở hữu
    private LocalDateTime purchaseTime; // Thời gian mua (để sort)
    private LocalDateTime expiryDate;   // Hạn sử dụng

    // Cập nhật Constructor nhận thêm ownerName
    public Ticket(String ticketId, double price, TicketType type, String ownerName) {
        this.ticketId = ticketId;
        this.price = price;
        this.type = type;
        this.ownerName = ownerName;
        
        this.status = TicketStatus.ACTIVE;
        this.purchaseTime = LocalDateTime.now(); // Lấy giờ hiện tại làm giờ mua
        
        // Tính hạn sử dụng
        this.expiryDate = calculateExpiryDate(type);
    }

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

    // --- LOGIC KIỂM TRA VÉ (GIỮ NGUYÊN) ---
    public boolean isValid() {
        // 1. Check Status
        if (status != TicketStatus.ACTIVE) {
            System.out.println("Validating Ticket " + ticketId + ": FAILED (Status is " + status + ")");
            return false;
        }
        
        // 2. Check Expiry
        if (LocalDateTime.now().isAfter(expiryDate)) {
            this.status = TicketStatus.EXPIRED;
            System.out.println("Validating Ticket " + ticketId + ": FAILED (Expired at " + expiryDate + ")");
            return false;
        }

        return true;
    }

    public void useTicket() {
        if (!isValid()) {
            System.out.println("Không thể sử dụng vé " + ticketId + ": Vé không hợp lệ hoặc đã hết hạn!");
            return;
        }

        if (type == TicketType.SINGLERIDE) {
            this.status = TicketStatus.USED;
            System.out.println("Vé " + ticketId + " (Vé lượt) đã sử dụng.");
        } else {
            System.out.println("Vé " + ticketId + " (" + type + ") đã quẹt. Mời qua cổng.");
        }
    }

    // --- GETTERS & SETTERS ---
    public String getTicketId() { return ticketId; }
    public double getPrice() { return price; }
    public TicketType getType() { return type; }
    public String getOwnerName() { return ownerName; }       // Getter mới cho UI
    public LocalDateTime getPurchaseTime() { return purchaseTime; } // Getter mới cho Sort
    public LocalDateTime getExpiryDate() { return expiryDate; }
    
    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    // Helper: Format giờ đẹp cho bảng Admin (VD: 04/01/2026 14:30:00)
    public String getFormattedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return purchaseTime.format(formatter);
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %.0f VND - Owner: %s", ticketId, type, price, ownerName);
    }
}