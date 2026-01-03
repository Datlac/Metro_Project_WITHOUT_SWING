package metro.models.device;

import metro.interfaces.IMaintainable;

public abstract class Device implements IMaintainable {
    protected String serialNumber;
    protected boolean isOnline;

    public Device(String serialNumber) {
        this.serialNumber = serialNumber;
        this.isOnline = true;
    }

    @Override
    public boolean checkStatus() {
        return isOnline;
    }

    @Override
    public void performMaintenance() {
        System.out.println("Performing maintenance on Device: " + serialNumber);
        this.isOnline = true;
    }
    
    @Override
    public int getHealthPercentage() {
        return isOnline ? 100 : 0;
    }
}