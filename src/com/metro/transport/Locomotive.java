package com.metro.transport;

public class Locomotive {
    private String locomotiveId;
    private double powerKW;
    private String manufacturer;

    public Locomotive(String locomotiveId, double powerKW, String manufacturer) {
        this.locomotiveId = locomotiveId;
        this.powerKW = powerKW;
        this.manufacturer = manufacturer;
    }
    
    public String getLocomotiveId() { return locomotiveId; }
}