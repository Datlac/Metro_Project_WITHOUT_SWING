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
    public String getDescription() { return description; }
}