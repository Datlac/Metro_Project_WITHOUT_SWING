package com.metro.business;

import java.util.ArrayList;
import java.util.List;

public class FarePolicy {
	private String policyId;
	private String policyName;
	private double pricePerKm;
	private boolean isActive;
	private List<String> rules;

	public FarePolicy(String policyId, String name, double pricePerKm) {
		this.policyId = policyId;
		this.policyName = name;
		this.pricePerKm = pricePerKm;
		this.isActive = true;
		this.rules = new ArrayList<>();
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getPolicyName() {
		return policyName;
	}

	public void setPolicyName(String policyName) {
		this.policyName = policyName;
	}

	public double getPricePerKm() {
		return pricePerKm;
	}

	public void setPricePerKm(double pricePerKm) {
		this.pricePerKm = pricePerKm;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public List<String> getRules() {
		return rules;
	}

	public void setRules(List<String> rules) {
		this.rules = rules;
	}

	public void addRule(String rule) {
		rules.add(rule);
	}
}