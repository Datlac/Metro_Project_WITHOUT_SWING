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
    
    public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public LocalDate getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(LocalDate plannedDate) {
		this.plannedDate = plannedDate;
	}

	public int getEstimatedDurationHours() {
		return estimatedDurationHours;
	}

	public void setEstimatedDurationHours(int estimatedDurationHours) {
		this.estimatedDurationHours = estimatedDurationHours;
	}

	public PriorityLevel getPriority() {
		return priority;
	}

	public void setPriority(PriorityLevel priority) {
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