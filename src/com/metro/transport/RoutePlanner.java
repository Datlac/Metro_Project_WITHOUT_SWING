package com.metro.transport;

import com.metro.infrastructure.Line;
import com.metro.infrastructure.Station;

import java.util.*;

public class RoutePlanner {
    private Map<String, List<Edge>> graph = new HashMap<>();
    private Map<String, String> stationNameMap = new HashMap<>();

    // Class kết quả trả về
    public static class RouteResult {
        public List<String> pathSteps;
        public double totalMinutes;
        public int totalTransfers; // Đếm số lần chuyển tuyến

        public RouteResult(List<String> pathSteps, double totalMinutes, int totalTransfers) {
            this.pathSteps = pathSteps;
            this.totalMinutes = totalMinutes;
            this.totalTransfers = totalTransfers;
        }
    }

    public void buildGraph(List<Line> lines) {
        graph.clear();
        stationNameMap.clear();

        for (Line line : lines) {
            List<Station> stations = line.getStations();
            String lineCode = line.getLineCode();
            
            double timePerStation;
            if (lineCode.startsWith("METRO")) {
                timePerStation = 2.5; 
            } else if (lineCode.equals("BUS-19")) {
                timePerStation = 60.0; // Bus 19 đi rất lâu
            } else {
                timePerStation = 12.0; 
            }

            for (int i = 0; i < stations.size() - 1; i++) {
                Station sU = stations.get(i);
                Station sV = stations.get(i + 1);
                
                String u = sU.getStationId();
                String v = sV.getStationId();
                
                stationNameMap.put(u, sU.getName());
                stationNameMap.put(v, sV.getName());
                
                String forwardName = line.getLineName();
                String backwardName = line.getLineName();

                if (lineCode.equals("METRO-01")) {
                    forwardName = "Metro: Ben Thanh -> Suoi Tien";
                    backwardName = "Metro: Suoi Tien -> Ben Thanh";
                }

                addEdge(u, v, timePerStation, forwardName);
                addEdge(v, u, timePerStation, backwardName);
            }
        }
    }

    private void addEdge(String u, String v, double time, String name) {
        graph.computeIfAbsent(u, k -> new ArrayList<>()).add(new Edge(v, time, name));
    }

    // --- THUẬT TOÁN 1: TÌM ĐƯỜNG NHANH NHẤT (THEO THỜI GIAN) ---
    public RouteResult findFastestPath(String startId, String endId) {
        return runDijkstra(startId, endId, true);
    }

    // --- THUẬT TOÁN 2: TÌM ĐƯỜNG ÍT CHUYỂN TUYẾN NHẤT ---
    public RouteResult findLeastTransferPath(String startId, String endId) {
        return runDijkstra(startId, endId, false);
    }

    // Hàm lõi chạy Dijkstra
    private RouteResult runDijkstra(String startId, String endId, boolean optimizeForTime) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Map<String, Double> dist = new HashMap<>();
        Map<String, String> prevNode = new HashMap<>();
        Map<String, String> prevLine = new HashMap<>();
        Map<String, Double> timeAccumulator = new HashMap<>(); // Để cộng dồn thời gian thực tế
        Map<String, Integer> transferCount = new HashMap<>();  // Để đếm số lần chuyển

        dist.put(startId, 0.0);
        timeAccumulator.put(startId, 0.0);
        transferCount.put(startId, 0);
        pq.add(new Node(startId, 0.0));

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            String u = current.id;

            if (u.equals(endId)) break; 

            if (graph.containsKey(u)) {
                for (Edge e : graph.get(u)) {
                    String lastLine = prevLine.get(u);
                    boolean isTransfer = lastLine != null && !lastLine.split(":")[0].equals(e.transportName.split(":")[0]);
                    
                    // --- LOGIC TÍNH TRỌNG SỐ (COST) ---
                    double stepCost;
                    double realTime = e.timeCost;
                    
                    if (optimizeForTime) {
                        // Ưu tiên thời gian: Cost = Thời gian đi + Phạt chuyển tuyến (8 phút)
                        stepCost = realTime;
                        if (isTransfer) {
                            stepCost += 8.0; 
                            realTime += 8.0; // Cộng vào thời gian thực tế
                        }
                    } else {
                        // Ưu tiên ít chuyển tuyến: 
                        // Cost đi 1 trạm = 1 điểm.
                        // Cost chuyển tuyến = 1000 điểm (Rất lớn để thuật toán né ra)
                        stepCost = 1.0; 
                        if (isTransfer) {
                            stepCost += 1000.0;
                            realTime += 8.0; // Vẫn cộng thời gian chờ vào thực tế
                        }
                    }

                    double newDist = dist.get(u) + stepCost;

                    if (newDist < dist.getOrDefault(e.targetId, Double.MAX_VALUE)) {
                        dist.put(e.targetId, newDist);
                        // Cập nhật thời gian thực tế để hiển thị
                        timeAccumulator.put(e.targetId, timeAccumulator.get(u) + realTime);
                        // Cập nhật số lần chuyển tuyến
                        int currentTransfers = transferCount.get(u);
                        if (isTransfer) currentTransfers++;
                        transferCount.put(e.targetId, currentTransfers);

                        prevNode.put(e.targetId, u);
                        prevLine.put(e.targetId, e.transportName);
                        pq.add(new Node(e.targetId, newDist));
                    }
                }
            }
        }
        
        List<String> path = reconstructPath(prevNode, prevLine, endId);
        double totalTime = timeAccumulator.getOrDefault(endId, 0.0);
        int transfers = transferCount.getOrDefault(endId, 0);
        
        return new RouteResult(path, totalTime, transfers);
    }

    private List<String> reconstructPath(Map<String, String> prevNode, Map<String, String> prevLine, String end) {
        LinkedList<String> path = new LinkedList<>();
        String curr = end;
        if (!prevNode.containsKey(curr)) return path;

        while (curr != null && prevNode.containsKey(curr)) {
            String p = prevNode.get(curr);
            String line = prevLine.get(curr);
            String stationName = stationNameMap.getOrDefault(curr, curr);
            path.addFirst(" (" + line + ") -> " + curr + " - " + stationName);
            curr = p;
        }
        if (curr != null) {
            String startName = stationNameMap.getOrDefault(curr, curr);
            path.addFirst(curr + " - " + startName);
        }
        return path;
    }

    private static class Node {
        String id; double cost;
        Node(String id, double cost) { this.id = id; this.cost = cost; }
    }
}