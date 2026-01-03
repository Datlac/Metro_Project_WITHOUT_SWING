package com.metro.maintenance;

import com.metro.enums.PriorityLevel; // Cần tạo Enum này
import java.time.LocalDate;

public class MaintenanceSchedule {
    private String scheduleId;
    private LocalDate plannedDate;
    private int estimatedDurationHours;
    private PriorityLevel priority;

    public MaintenanceSchedule(String scheduleId, LocalDate plannedDate, int duration, PriorityLevel priority) {
        this.scheduleId = scheduleId;
        this.plannedDate = plannedDate;
        this.estimatedDurationHours = duration;
        this.priority = priority;
    }

    public boolean isOverdue() {
        return LocalDate.now().isAfter(plannedDate);
    }

    public void reschedule(LocalDate newDate) {
        this.plannedDate = newDate;
        System.out.println("Rescheduled to " + newDate);
    }
}