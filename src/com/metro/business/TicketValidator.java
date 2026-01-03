package com.metro.business;

import java.util.List;
import java.util.Optional;

public class TicketValidator {

    // Giả lập database vé (Trong thực tế sẽ query DB)
    public static boolean validateTicketId(String ticketId, List<Ticket> systemTickets) {
        // Tìm vé trong danh sách
        Optional<Ticket> ticketOpt = systemTickets.stream()
                .filter(t -> t.getTicketId().equals(ticketId))
                .findFirst();

        if (ticketOpt.isPresent()) {
            return ticketOpt.get().isValid(); // Gọi hàm isValid() đã viết ở trên
        } else {
            System.out.println("Error: Ticket ID " + ticketId + " not found in system.");
            return false;
        }
    }
}