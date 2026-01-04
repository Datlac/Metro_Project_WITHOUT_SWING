package com.metro.infrastructure;

import java.util.List;

public class Route {
    private String routeId;
    private String description;
    private Station startStation;
    private Station endStation;
    private List<Station> stops;

    public Route(String routeId, String description, Station start, Station end, List<Station> stops) {
        this.routeId = routeId;
        this.description = description;
        this.startStation = start;
        this.endStation = end;
        this.stops = stops;
    }
    
    // Getters
    public String getRouteId() { return routeId; }
    public Station getStartStation() {
		return startStation;
	}

	public void setStartStation(Station startStation) {
		this.startStation = startStation;
	}

	public Station getEndStation() {
		return endStation;
	}

	public void setEndStation(Station endStation) {
		this.endStation = endStation;
	}

	public List<Station> getStops() {
		return stops;
	}

	public void setStops(List<Station> stops) {
		this.stops = stops;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() { return description; }
}