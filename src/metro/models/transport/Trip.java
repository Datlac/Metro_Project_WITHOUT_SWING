package metro.models.transport;
import java.time.LocalDateTime;

import metro.enums.TripStatus;
import metro.models.person.Driver;

import java.time.Duration;

public class Trip {
    private String tripId;
    private Route route;           // Chuyến đi thuộc lộ trình nào
    private Train train;           // Tàu nào thực hiện
    private Driver driver;         // Tài xế nào lái
    private LocalDateTime departureTime; // Thời gian khởi hành dự kiến
    private LocalDateTime arrivalTime;   // Thời gian đến dự kiến
    private TripStatus status;     // Trạng thái (SCHEDULED, ON_GOING...)

    // Constructor đầy đủ
    public Trip(String tripId, Route route, Train train, Driver driver, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.tripId = tripId;
        this.route = route;
        this.train = train;
        this.driver = driver;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.status = TripStatus.SCHEDULED;
    }

    // Constructor đơn giản (nếu chưa xếp tài xế/tàu ngay)
    public Trip(String tripId, Route route, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this(tripId, route, null, null, departureTime, arrivalTime);
    }

    // --- CÁC PHƯƠNG THỨC CHỨC NĂNG ---

    // Gán tài xế cho chuyến
    public void assignDriver(Driver driver) {
        this.driver = driver;
    }

    // Gán tàu cho chuyến
    public void assignTrain(Train train) {
        this.train = train;
    }

    // Bắt đầu chuyến đi
    public void startTrip() {
        if (this.train != null && this.driver != null) {
            this.status = TripStatus.ON_GOING;
            System.out.println("Chuyến " + tripId + " đã khởi hành lúc " + LocalDateTime.now());
        } else {
            System.out.println("Không thể khởi hành: Chưa có tàu hoặc tài xế!");
        }
    }

    // Kết thúc chuyến đi
    public void completeTrip() {
        this.status = TripStatus.COMPLETED;
        System.out.println("Chuyến " + tripId + " đã hoàn thành.");
    }

    // Tính thời gian dự kiến (Duration)
    public long getDurationMinutes() {
        if (departureTime != null && arrivalTime != null) {
            return Duration.between(departureTime, arrivalTime).toMinutes();
        }
        return 0;
    }

    // --- GETTERS & SETTERS ---
    public String getTripId() { return tripId; }
    public TripStatus getStatus() { return status; }
    public Route getRoute() { return route; }
    public LocalDateTime getDepartureTime() { return departureTime; }

    @Override
    public String toString() {
        String driverName = (driver != null) ? driver.getFullName() : "Chưa có";
        String routeName = (route != null) ? route.toString() : "Chưa có";
        return String.format("Trip[%s] | Route: %s | Driver: %s | Time: %s -> %s | Status: %s", 
                tripId, routeName, driverName, departureTime, arrivalTime, status);
    }
}