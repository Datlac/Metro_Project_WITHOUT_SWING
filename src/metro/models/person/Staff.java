package metro.models.person;
public class Staff extends Person {
    private String idStaff;
    private String department;
    private String jobTitle;

    public Staff(String fullName, String idNumber, java.time.LocalDate dob, String phoneNumber, 
                 String idStaff, String department, String jobTitle) {
        super(fullName, idNumber, dob, phoneNumber);
        this.idStaff = idStaff;
        this.department = department;
        this.jobTitle = jobTitle;
    }

    // Getters/Setters nếu cần
    public String getIdStaff() { return idStaff; }
}