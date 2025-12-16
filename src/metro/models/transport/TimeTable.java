package metro.models.transport;
import java.time.LocalDate;
import java.util.ArrayList;

public class TimeTable {
    private String timeTableId;
    private String lineCode;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private String seasonType;

    public TimeTable(String id, String lineCode) {
        this.timeTableId = id;
        this.lineCode = lineCode;
    }

    public ArrayList<Trip> getTripsByHour(int hour) {
        return new ArrayList<>(); // Trả về danh sách chuyến
    }
}