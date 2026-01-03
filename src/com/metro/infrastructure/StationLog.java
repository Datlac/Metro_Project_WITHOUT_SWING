package com.metro.infrastructure;

import java.time.LocalDateTime;

public class StationLog {
    private String logId;
    private LocalDateTime timestamp;
    private String stationId;
    private String details;
    private String eventType; // e.g., "PASSENGER_ENTRY", "MAINTENANCE"

    public StationLog(String logId, String stationId, String eventType, String details) {
        this.logId = logId;
        this.stationId = stationId;
        this.eventType = eventType;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] Station: %s | Event: %s | %s", timestamp, stationId, eventType, details);
    }
}