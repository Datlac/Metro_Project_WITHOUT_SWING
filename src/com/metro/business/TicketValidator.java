package com.metro.business;

import java.util.List;
import java.util.Optional;

public class TicketValidator {

	public static boolean validateTicketId(String ticketId, List<Ticket> systemTickets) {

		Optional<Ticket> ticketOpt = systemTickets.stream().filter(t -> t.getTicketId().equals(ticketId)).findFirst();

		if (ticketOpt.isPresent()) {
			return ticketOpt.get().isValid();
		} else {
			System.out.println("Error: Ticket ID " + ticketId + " not found in system.");
			return false;
		}
	}
}