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
    
    
    public double getPowerKW() {
		return powerKW;
	}


	public void setPowerKW(double powerKW) {
		this.powerKW = powerKW;
	}


	public String getManufacturer() {
		return manufacturer;
	}


	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}


	public void setLocomotiveId(String locomotiveId) {
		this.locomotiveId = locomotiveId;
	}


	public String getLocomotiveId() { return locomotiveId; }
}