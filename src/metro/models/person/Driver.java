package metro.models.person;
import java.time.LocalDate;

public class Driver extends Staff {
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private float totalDrivingHours;

    public Driver(String fullName, String idNumber, LocalDate dob, String phoneNumber, 
                  String idStaff, String department, String jobTitle,
                  String licenseNumber, LocalDate licenseExpiryDate) {
        super(fullName, idNumber, dob, phoneNumber, idStaff, department, jobTitle);
        this.licenseNumber = licenseNumber;
        this.licenseExpiryDate = licenseExpiryDate;
        this.totalDrivingHours = 0;
    }
}