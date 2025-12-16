package metro.models.device;
public class Device {
    protected String serialNumber;
    protected boolean isOnline;

    public Device(String serialNumber) {
        this.serialNumber = serialNumber;
        this.isOnline = true;
    }
}