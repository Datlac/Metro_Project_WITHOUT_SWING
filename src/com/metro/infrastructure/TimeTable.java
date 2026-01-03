package com.metro.infrastructure;

import com.metro.transport.Trip;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TimeTable {
    private String timeTableId;
    private String lineCode;
    private LocalDate effectiveDate;
    private LocalDate expirationDate;
    private String seasonType;
    private List<Trip> trips;

    public TimeTable(String timeTableId, String lineCode, LocalDate effectiveDate) {
        this.timeTableId = timeTableId;
        this.lineCode = lineCode;
        this.effectiveDate = effectiveDate;
        this.trips = new ArrayList<>();
    }

    public void addTrip(Trip trip) {
        this.trips.add(trip);
    }

    // Java 8: Lọc chuyến đi theo giờ
    public List<Trip> getTripsByHour(int hour) {
        return trips.stream()
                .filter(t -> t.getDepartureTime().getHour() == hour)
                .collect(Collectors.toList());
    }
}