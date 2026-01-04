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
    
    public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	@Override
    public String toString() {
        return String.format("[%s] Station: %s | Event: %s | %s", timestamp, stationId, eventType, details);
    }
}