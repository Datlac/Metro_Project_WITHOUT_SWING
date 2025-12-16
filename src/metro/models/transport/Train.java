package metro.models.transport;
import java.util.ArrayList;
import java.util.List;

import metro.enums.TrainStatus;

public class Train {
    private String trainId;
    private int capacity;
    private double currentSpeed;
    private int totalDistanceRun;
    private TrainStatus status;

    private Locomotive locomotive;
    private List<Carriage> carriages;

    public Train(String trainId) {
        this.trainId = trainId;
        this.status = TrainStatus.RUNNING;
        this.carriages = new ArrayList<>();
    }
    
    public Train(String trainId, int capacity) {
        this.trainId = trainId;
        this.capacity = capacity;
        this.status = TrainStatus.RUNNING;
        this.carriages = new ArrayList<>();
    }

    public void setLocomotive(Locomotive loc) {
        this.locomotive = loc;
    }
    
    public void addCarriage(Carriage car) {
        this.carriages.add(car);
    }

    public double calculateTotalWeight() {
        return 50000; // Giả định
    }
    
    public int calculateTotalCapacity() {
        int total = 0;
        for (Carriage c : carriages) {
            total += c.getTotalCapacity();
        }
        return total;
    }
    
    public String getTrainId() { return trainId; }

    @Override
    public String toString() {
        return "Train " + trainId + " (Gồm " + carriages.size() + " toa)";
    }
}