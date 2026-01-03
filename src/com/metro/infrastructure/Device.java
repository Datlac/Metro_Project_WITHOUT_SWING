package com.metro.infrastructure;

public class Device {
    protected String serialNumber;
    protected boolean isOnline;

    public Device(String serialNumber) {
        this.serialNumber = serialNumber;
        this.isOnline = true;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}