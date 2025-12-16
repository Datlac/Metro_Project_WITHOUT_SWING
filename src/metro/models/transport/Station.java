package metro.models.transport;
import metro.enums.StationStatus;

public class Station {
    private String stationId;
    private String stationName;
    private StationStatus status;

    public Station(String stationId, String stationName) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.status = StationStatus.OPEN;
    }

    public String getStationId() { return stationId; }
    public String getStationName() { return stationName; }
    
    @Override
    public String toString() { return stationName; }
}