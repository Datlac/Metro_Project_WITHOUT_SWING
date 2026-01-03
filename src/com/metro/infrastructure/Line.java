package com.metro.infrastructure;

import com.metro.enums.LineStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Line {
    private String lineCode;
    private String lineName;
    private LineStatus status;
    private List<Station> stations;

    public Line(String lineCode, String lineName) {
        this.lineCode = lineCode;
        this.lineName = lineName;
        
        // CẬP NHẬT: Sử dụng OPERATING thay vì OPERATIONAL
        this.status = LineStatus.OPERATING;
        
        this.stations = new ArrayList<>();
    }

    public void addStation(Station station) {
        if (!stations.contains(station)) {
            stations.add(station);
        }
    }

    public boolean removeStation(String stationId) {
        return stations.removeIf(s -> s.getStationId().equals(stationId));
    }

    public Optional<Station> findStationById(String stationId) {
        return stations.stream()
                .filter(s -> s.getStationId().equals(stationId))
                .findFirst();
    }

    // Getters Setters
    public String getLineCode() { return lineCode; }
    public String getLineName() { return lineName; }
    public LineStatus getStatus() { return status; }
    
    public void setStatus(LineStatus status) { 
        this.status = status; 
    }
}