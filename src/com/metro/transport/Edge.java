package com.metro.transport;

public class Edge {
	public String targetId;
	public double timeCost;
	public String transportName;

	public Edge(String targetId, double timeCost, String transportName) {
		this.targetId = targetId;
		this.timeCost = timeCost;
		this.transportName = transportName;
	}
}