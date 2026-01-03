package com.metro.app;

import com.metro.infrastructure.Line;
import com.metro.people.Staff;
import com.metro.transport.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetroSystem {
    private static MetroSystem instance;
    private String systemName;
    private List<Line> lines;
    private List<Train> fleet;
    private List<Staff> staffList;

    private MetroSystem(String systemName) {
        this.systemName = systemName;
        this.lines = new ArrayList<>();
        this.fleet = new ArrayList<>();
        this.staffList = new ArrayList<>();
    }

    // Singleton Pattern
    public static synchronized MetroSystem getInstance() {
        if (instance == null) {
            instance = new MetroSystem("Ho Chi Minh City Metro");
        }
        return instance;
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void addTrain(Train train) {
        fleet.add(train);
    }

    public void addStaff(Staff staff) {
        staffList.add(staff);
    }

    // Java 8: Tìm kiếm Staff
    public Optional<Staff> findStaffById(String id) {
        return staffList.stream()
                .filter(s -> s.getIdStaff().equals(id))
                .findFirst();
    }
}