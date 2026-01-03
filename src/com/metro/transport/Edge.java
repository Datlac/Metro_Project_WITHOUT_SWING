package com.metro.transport;

public class Edge {
    public String targetStationId;
    public double timeCost; // Thời gian di chuyển (phút)
    public String lineCode; // Tên tuyến (VD: Bus 19, Metro Line 1)

    public Edge(String targetStationId, double timeCost, String lineCode) {
        this.targetStationId = targetStationId;
        this.timeCost = timeCost;
        this.lineCode = lineCode;
    }
}