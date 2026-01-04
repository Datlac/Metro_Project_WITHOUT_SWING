package com.metro.people;

import java.time.LocalDate;
import java.time.Period;

public class Person {
	protected String fullName;
	protected String idNumber;
	protected LocalDate dob;
	protected String phoneNumber;

	public Person(String fullName, String idNumber, LocalDate dob, String phoneNumber) {
		this.fullName = fullName;
		this.idNumber = idNumber;
		this.dob = dob;
		this.phoneNumber = phoneNumber;
	}

	// Java 8:
	public int getAge() {
		return (dob != null) ? Period.between(dob, LocalDate.now()).getYears() : 0;
	}

	public String getFullName() {
		return fullName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}