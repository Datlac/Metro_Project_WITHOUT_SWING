package metro.models.device;
import java.time.LocalDateTime;

import metro.enums.GateStatus;
import metro.enums.ScanResult;
import metro.models.finance.Ticket;
import metro.models.transport.Station;

public class AutoGate extends Device {
    private String gateId;
    private GateStatus status;
    private Station location;
    private LocalDateTime lastUsed;
    private boolean sensorActive;

    public AutoGate(String serialNumber, String gateId, Station location) {
        super(serialNumber);
        this.gateId = gateId;
        this.location = location;
        this.status = GateStatus.CLOSED;
        this.sensorActive = true;
    }

    public ScanResult scanTicket(Ticket ticket) {
        if (ticket.isValid()) {
            this.lastUsed = LocalDateTime.now();
            openGate();
            return ScanResult.GRANTED;
        }
        return ScanResult.DENIED;
    }

    public void openGate() {
        this.status = GateStatus.OPEN;
        System.out.println("Gate " + gateId + " opened.");
    }

    public void closeGate() {
        this.status = GateStatus.CLOSED;
        System.out.println("Gate " + gateId + " closed.");
    }
}