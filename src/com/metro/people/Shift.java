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
    
    // Getters...
}