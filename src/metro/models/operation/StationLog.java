package metro.models.operation;

import metro.models.transport.Station;
import metro.models.finance.Ticket;
import java.time.LocalDateTime;

public class StationLog {
    private String logId;
    private LocalDateTime timestamp;
    private Station station;
    private Ticket ticket;
    private boolean isEntry; // True: Vào ga, False: Ra ga

    public StationLog(String logId, Station station, Ticket ticket, boolean isEntry) {
        this.logId = logId;
        this.station = station;
        this.ticket = ticket;
        this.isEntry = isEntry;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String action = isEntry ? "CHECK-IN" : "CHECK-OUT";
        return String.format("[%s] %s at %s | Ticket: %s", 
                timestamp, action, station.getStationName(), ticket.getTicketId());
    }
}