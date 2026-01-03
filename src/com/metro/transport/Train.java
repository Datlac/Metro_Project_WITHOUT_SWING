package com.metro.transport;

public class Train {
    private String trainId;
    private int capacity;

    public Train(String trainId, int capacity) {
        this.trainId = trainId;
        this.capacity = capacity;
    }
    
    @Override
    public String toString() { return "Train " + trainId; }
}