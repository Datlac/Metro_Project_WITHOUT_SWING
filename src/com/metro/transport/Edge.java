package com.metro.transport;

public class Edge {
    public String targetId; // Trạm đích
    public double timeCost; // Thời gian di chuyển (phút)
    public String transportName; // Tên phương tiện (Bus 56, Metro Line 1...)

    public Edge(String targetId, double timeCost, String transportName) {
        this.targetId = targetId;
        this.timeCost = timeCost;
        this.transportName = transportName;
    }
}