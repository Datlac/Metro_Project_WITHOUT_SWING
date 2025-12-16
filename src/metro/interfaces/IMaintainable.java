package metro.interfaces;

public interface IMaintainable {
    boolean checkStatus();
    void performMaintenance();
    int getHealthPercentage();
}