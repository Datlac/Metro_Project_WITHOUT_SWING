package metro.models.operation;
import java.sql.Time;

public class Shift {
    private String shiftCode;
    private Time startTime;
    private Time endTime;
    private String notes;

    public Shift(String shiftCode, Time start, Time end) {
        this.shiftCode = shiftCode;
        this.startTime = start;
        this.endTime = end;
    }
}