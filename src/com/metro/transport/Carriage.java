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

	public String getCarriageId() {
		return carriageId;
	}

	public void setCarriageId(String carriageId) {
		this.carriageId = carriageId;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public int getStandingCapacity() {
		return standingCapacity;
	}

	public void setStandingCapacity(int standingCapacity) {
		this.standingCapacity = standingCapacity;
	}

	public boolean isHasWifi() {
		return hasWifi;
	}

	public void setHasWifi(boolean hasWifi) {
		this.hasWifi = hasWifi;
	}

	public int getTotalCapacity() {
		return seatCapacity + standingCapacity;
	}
}