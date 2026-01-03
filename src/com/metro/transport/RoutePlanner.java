package com.metro.transport;

import com.metro.infrastructure.Line;
import com.metro.infrastructure.Station;

import java.util.*;

public class RoutePlanner {

    // Đồ thị: Tên Trạm -> Danh sách các cạnh nối (đi đâu, mất bao lâu, tuyến nào)
    private Map<String, List<Edge>> adjacencyList;

    public RoutePlanner() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Xây dựng đồ thị có trọng số (Thời gian)
     */
    public void buildGraph(List<Line> lines) {
        adjacencyList.clear();

        for (Line line : lines) {
            List<Station> stations = line.getStations();
            if (stations.size() < 2) continue;

            // Giả lập thời gian di chuyển:
            // - Metro: Rất nhanh (2 phút/trạm)
            // - Bus: Chậm hơn (5-10 phút/trạm tùy khoảng cách)
            double timePerStation = line.getLineCode().startsWith("METRO") ? 2.0 : 10.0;

            for (int i = 0; i < stations.size() - 1; i++) {
                String currentId = stations.get(i).getStationId();
                String nextId = stations.get(i + 1).getStationId();

                // Thêm cạnh 2 chiều (Vô hướng)
                addEdge(currentId, nextId, timePerStation, line.getLineCode());
                addEdge(nextId, currentId, timePerStation, line.getLineCode());
            }
        }
    }

    private void addEdge(String from, String to, double time, String lineCode) {
        adjacencyList.computeIfAbsent(from, k -> new ArrayList<>()).add(new Edge(to, time, lineCode));
    }

    /**
     * THUẬT TOÁN DIJKSTRA: Tìm đường đi nhanh nhất
     */
    public List<String> findShortestPath(String startId, String endId) {
        // Lưu thời gian ngắn nhất đến từng trạm (Mặc định là vô cực)
        Map<String, Double> minTime = new HashMap<>();
        // Lưu vết đường đi: Đến trạm Key từ trạm Value
        Map<String, String> previousStation = new HashMap<>();
        // Lưu vết tuyến xe: Đến trạm Key bằng Bus/Metro nào
        Map<String, String> usedLine = new HashMap<>();

        // Hàng đợi ưu tiên: Luôn xử lý trạm có tổng thời gian nhỏ nhất trước
        PriorityQueue<NodeCost> pq = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));

        // Khởi tạo
        minTime.put(startId, 0.0);
        pq.add(new NodeCost(startId, 0.0));

        while (!pq.isEmpty()) {
            NodeCost current = pq.poll();
            String u = current.id;

            if (u.equals(endId)) break; // Đã đến đích

            // Nếu tìm thấy đường khác dài hơn đường hiện tại thì bỏ qua
            if (current.cost > minTime.getOrDefault(u, Double.MAX_VALUE)) continue;

            // Duyệt các trạm kề
            if (adjacencyList.containsKey(u)) {
                for (Edge edge : adjacencyList.get(u)) {
                    String v = edge.targetStationId;
                    double newDist = minTime.get(u) + edge.timeCost;

                    // Nếu tìm thấy đường nhanh hơn đến v
                    if (newDist < minTime.getOrDefault(v, Double.MAX_VALUE)) {
                        minTime.put(v, newDist);
                        previousStation.put(v, u);
                        usedLine.put(v, edge.lineCode); // Lưu lại là đi bằng tuyến nào
                        pq.add(new NodeCost(v, newDist));
                    }
                }
            }
        }

        return reconstructPath(previousStation, usedLine, startId, endId);
    }

    private List<String> reconstructPath(Map<String, String> prev, Map<String, String> lines, String start, String end) {
        List<String> path = new LinkedList<>();
        String curr = end;
        
        if (!prev.containsKey(curr) && !curr.equals(start)) return path; // Không tìm thấy

        while (curr != null) {
            String p = prev.get(curr);
            String lineInfo = lines.get(curr);
            
            if (p != null) {
                // Format: [Tên Tuyến] Trạm
                path.add(0, String.format("(%s) -> %s", lineInfo, curr));
            } else {
                path.add(0, curr); // Điểm bắt đầu
            }
            curr = p;
        }
        return path;
    }

    // Class phụ hỗ trợ PriorityQueue
    private static class NodeCost {
        String id;
        double cost;
        public NodeCost(String id, double cost) { this.id = id; this.cost = cost; }
    }
}