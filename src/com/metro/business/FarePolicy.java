package com.metro.business;

import java.util.ArrayList;
import java.util.List;

public class FarePolicy {
    private String policyId;
    private String policyName;
    private double pricePerKm;
    private boolean isActive;
    // DiscountRule là class phụ hoặc String mô tả quy tắc
    private List<String> rules; 

    public FarePolicy(String policyId, String name, double pricePerKm) {
        this.policyId = policyId;
        this.policyName = name;
        this.pricePerKm = pricePerKm;
        this.isActive = true;
        this.rules = new ArrayList<>();
    }

    public void addRule(String rule) {
        rules.add(rule);
    }
}