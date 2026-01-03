package com.metro.people;

import java.time.LocalTime;

public class Shift {
    private String shiftCode;
    private LocalTime startTime;
    private LocalTime endTime;
    private String notes;

    public Shift(String shiftCode, LocalTime startTime, LocalTime endTime, String notes) {
        this.shiftCode = shiftCode;
        this.startTime = startTime;
        this.endTime = endTime;
        this.notes = notes;
    }

	public String getShiftCode() {
		return shiftCode;
	}

	public void setShiftCode(String shiftCode) {
		this.shiftCode = shiftCode;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
    
    
}