package com.metro.people;

import java.time.LocalDate;

public class Driver extends Person {
	private String licenseNumber;
	private String staffId;

	public Driver(String fullName, String idNumber, LocalDate dob, String phoneNumber, String staffId,
			String licenseNumber) {
		super(fullName, idNumber, dob, phoneNumber);
		this.staffId = staffId;
		this.licenseNumber = licenseNumber;
	}

	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

}