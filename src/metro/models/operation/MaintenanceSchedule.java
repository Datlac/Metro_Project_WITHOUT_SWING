package metro.models.operation;
import java.time.LocalDate;

import metro.enums.PriorityLevel;

public class MaintenanceSchedule {
    private String scheduleId;
    private LocalDate plannedDate;
    private int estimatedDurationHours;
    private PriorityLevel priority;

    public MaintenanceSchedule(String id, LocalDate date, PriorityLevel priority) {
        this.scheduleId = id;
        this.plannedDate = date;
        this.priority = priority;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(plannedDate);
    }
}