package com.metro.transport;

public class Carriage {
    private String carriageId;
    private int seatCapacity;
    private int standingCapacity;
    private boolean hasWifi;

    public Carriage(String carriageId, int seatCapacity, int standingCapacity) {
        this.carriageId = carriageId;
        this.seatCapacity = seatCapacity;
        this.standingCapacity = standingCapacity;
        this.hasWifi = true;
    }

    public int getTotalCapacity() {
        return seatCapacity + standingCapacity;
    }
}