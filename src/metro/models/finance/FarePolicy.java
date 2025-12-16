package metro.models.finance;
import java.util.ArrayList;

public class FarePolicy {
    private String policyId;
    private String policyName;
    private double pricePerKm;
    private boolean isActive;
    // Giả sử DiscountRule là một class khác hoặc String mô tả
    private ArrayList<String> rules; 

    public FarePolicy(String id, String name, double price) {
        this.policyId = id;
        this.policyName = name;
        this.pricePerKm = price;
        this.isActive = true;
        this.rules = new ArrayList<>();
    }
}