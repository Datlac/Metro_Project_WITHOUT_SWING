package com.metro.transport;

public class Train {
    private String trainId;
    private int capacity;

    public Train(String trainId, int capacity) {
        this.trainId = trainId;
        this.capacity = capacity;
    }
    
    public String getTrainId() {
		return trainId;
	}

	public void setTrainId(String trainId) {
		this.trainId = trainId;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	@Override
    public String toString() { return "Train " + trainId; }
}