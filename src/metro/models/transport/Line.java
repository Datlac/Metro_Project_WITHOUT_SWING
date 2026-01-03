package metro.models.transport;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import metro.enums.LineStatus;

public class Line {
    private String lineCode;
    private String name;
    private LineStatus status;
    private List<Station> orderedStations;
    private Map<String, Station> stationsById;
    // TreeMap: Key is sorted automatically
    private Map<String, Route> routesById; 

    public Line(String lineCode, String name) {
        this.lineCode = lineCode;
        this.name = name;
        this.status = LineStatus.OPERATING;
        this.orderedStations = new LinkedList<>();
        this.stationsById = new HashMap<>();
        this.routesById = new TreeMap<>(); 
    }

    public void addStation(Station st) {
        orderedStations.add(st);
        stationsById.put(st.getStationId(), st);
    }

    public void addRoute(String id, Route r) {
        routesById.put(id, r);
    }

    public void showRoutes() {
        System.out.println("Danh sách tuyến đường (Sắp xếp theo ID - TreeMap):");
        routesById.forEach((k, v) -> System.out.println(" - " + k + ": " + v));
    }
}