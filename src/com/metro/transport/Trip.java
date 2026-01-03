package com.metro.transport;

import com.metro.people.Driver;
import com.metro.enums.TripStatus;
import com.metro.infrastructure.Route; // Giả sử đã có lớp Route
import java.time.LocalDateTime;

public class Trip {
    private String tripId;
    private Train train;
    private Driver driver;
    private TripStatus status;
    private LocalDateTime departureTime; // Thời gian khởi hành dự kiến/thực tế
    private LocalDateTime arrivalTime;   // Thời gian đến nơi
    private Route route;

    public Trip(String tripId, Train train, Driver driver, Route route, LocalDateTime departureTime) {
        this.tripId = tripId;
        this.train = train;
        this.driver = driver;
        this.route = route;
        this.departureTime = departureTime;
        this.status = TripStatus.SCHEDULED;
    }

    // Constructor đơn giản hơn nếu chưa có Route/Time lúc khởi tạo
    public Trip(String tripId, Train train, Driver driver) {
        this.tripId = tripId;
        this.train = train;
        this.driver = driver;
        this.status = TripStatus.SCHEDULED;
        this.departureTime = LocalDateTime.now().plusMinutes(15); // Mặc định 15p nữa chạy
    }

    public void start() {
        this.status = TripStatus.ON_GOING;
        // Cập nhật lại thời gian khởi hành thực tế là lúc gọi hàm start
        this.departureTime = LocalDateTime.now(); 
        System.out.println("Trip " + tripId + " started at " + departureTime);
    }

    public void complete() {
        this.status = TripStatus.COMPLETED;
        this.arrivalTime = LocalDateTime.now();
    }

    // --- GETTERS (Quan trọng: Cần có phương thức này cho TimeTable) ---
    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public TripStatus getStatus() {
        return status;
    }

    public String getTripId() {
        return tripId;
    }
}