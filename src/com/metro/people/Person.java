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

    // Java 8: Sử dụng Period để tính tuổi chính xác
    public int getAge() {
        return (dob != null) ? Period.between(dob, LocalDate.now()).getYears() : 0;
    }

    public String getFullName() { return fullName; }
}