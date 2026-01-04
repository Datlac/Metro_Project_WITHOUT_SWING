package com.metro.business;

import com.metro.enums.CustomerType;

public class DiscountRule {
    private String ruleCode;
    private CustomerType appliedTo;
    private double discountPercentage; // e.g., 0.5 for 50%

    public DiscountRule(String ruleCode, CustomerType appliedTo, double discountPercentage) {
        this.ruleCode = ruleCode;
        this.appliedTo = appliedTo;
        this.discountPercentage = discountPercentage;
    }
    
    
    public String getRuleCode() {
		return ruleCode;
	}


	public void setRuleCode(String ruleCode) {
		this.ruleCode = ruleCode;
	}


	public double getDiscountPercentage() {
		return discountPercentage;
	}


	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}


	public void setAppliedTo(CustomerType appliedTo) {
		this.appliedTo = appliedTo;
	}


	public double calculateDiscount(double originalPrice) {
        return originalPrice * discountPercentage;
    }
    
    public CustomerType getAppliedTo() { return appliedTo; }
}