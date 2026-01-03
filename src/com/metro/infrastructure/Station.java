package com.metro.infrastructure;

import com.metro.enums.StationStatus;

public class Station {
    private String stationId;
    private String name;
    private StationStatus status;

    public Station(String stationId, String name) {
        this.stationId = stationId;
        this.name = name;
        this.status = StationStatus.OPEN;
    }

    public String getStationId() { return stationId; }
    public String getName() { return name; }

	public StationStatus getStatus() {
		return status;
	}

	public void setStatus(StationStatus status) {
		this.status = status;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}