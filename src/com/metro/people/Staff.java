package com.metro.people;

import java.time.LocalDate;

public class Staff extends Person {
    private String idStaff;
    private String department;
    private String jobTitle;
    private LocalDate hireDate;

    public Staff(String fullName, String idNumber, LocalDate dob, String phoneNumber, 
                 String idStaff, String department, String jobTitle) {
        super(fullName, idNumber, dob, phoneNumber);
        this.idStaff = idStaff;
        this.department = department;
        this.jobTitle = jobTitle;
        this.hireDate = LocalDate.now();
    }

    public String getIdStaff() { return idStaff; }
    public String getDepartment() { return department; }
    public String getJobTitle() { return jobTitle; }
}