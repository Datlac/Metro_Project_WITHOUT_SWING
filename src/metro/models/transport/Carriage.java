package metro.models.transport;

import metro.models.device.Device;

public class Carriage extends Device {
    private int seatCapacity;    // Số ghế ngồi
    private int standCapacity;   // Số chỗ đứng
    private boolean hasAirConditioner;

    public Carriage(String serialNumber, int seatCapacity, int standCapacity) {
        super(serialNumber);
        this.seatCapacity = seatCapacity;
        this.standCapacity = standCapacity;
        this.hasAirConditioner = true;
    }

    public int getTotalCapacity() {
        return seatCapacity + standCapacity;
    }

    @Override
    public String toString() {
        return "Carriage [" + serialNumber + "] - Cap: " + getTotalCapacity();
    }
}