package com.metro.infrastructure;

import com.metro.business.Ticket;
import com.metro.enums.GateStatus;

public class AutoGate extends Device {
	private String gateId;
	private GateStatus status;

	public AutoGate(String gateId, String serialNumber) {
		super(serialNumber);
		this.gateId = gateId;
		this.status = GateStatus.CLOSED;
	}

	public String getGateId() {
		return gateId;
	}

	public void setGateId(String gateId) {
		this.gateId = gateId;
	}

	public GateStatus getStatus() {
		return status;
	}

	public void setStatus(GateStatus status) {
		this.status = status;
	}

	public void scanTicket(Ticket ticket) {
		if (ticket.isValid()) {
			this.status = GateStatus.OPEN;
			System.out.println("[Gate " + gateId + "] Ticket " + ticket.getTicketId() + " accepted. OPEN.");
			ticket.useTicket();
		} else {
			System.out.println("[Gate " + gateId + "] Ticket invalid. CLOSED.");
		}
	}
}