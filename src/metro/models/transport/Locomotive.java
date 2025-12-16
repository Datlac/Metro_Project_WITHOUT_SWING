package metro.models.transport;

import metro.models.device.Device;

public class Locomotive extends Device {
    private double powerKW;      // Công suất kéo
    private String fuelType;     // Loại nhiên liệu (Electric/Diesel)
    private double maxSpeed;

    public Locomotive(String serialNumber, double powerKW, String fuelType, double maxSpeed) {
        super(serialNumber); // Kế thừa từ Device (có serialNumber, isOnline)
        this.powerKW = powerKW;
        this.fuelType = fuelType;
        this.maxSpeed = maxSpeed;
    }

    public double getPowerKW() { return powerKW; }

    @Override
    public String toString() {
        return "Locomotive [" + serialNumber + "] - " + powerKW + "kW";
    }
}