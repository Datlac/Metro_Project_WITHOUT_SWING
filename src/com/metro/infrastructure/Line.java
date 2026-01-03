package com.metro.infrastructure;

import com.metro.enums.LineStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Line {
    private String lineCode;
    private String lineName;
    private LineStatus status;
    private List<Station> stations;

    public Line(String lineCode, String lineName) {
        this.lineCode = lineCode;
        this.lineName = lineName;
        this.status = LineStatus.OPERATING; // Mặc định là đang hoạt động
        this.stations = new ArrayList<>();
    }

    public void addStation(Station station) {
        // Tránh thêm trùng lặp
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

    // --- CÁC GETTER CẦN THIẾT (Đã bổ sung getStations) ---

    // Phương thức này dùng cho RoutePlanner để duyệt qua các trạm
    public List<Station> getStations() {
        return stations;
    }

    public String getLineCode() {
        return lineCode;
    }

    public String getLineName() {
        return lineName;
    }

    public LineStatus getStatus() {
        return status;
    }

    public void setStatus(LineStatus status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return lineName + " (" + lineCode + ") - " + stations.size() + " stations";
    }
}