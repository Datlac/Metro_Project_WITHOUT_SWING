package metro.models.person;
import java.time.LocalDate;
import java.util.Date;

import metro.enums.CustomerType;

public class RegisteredCustomer extends Customer {
    private String username;
    private String passwordHash;
    private Date registrationDate;

    public RegisteredCustomer(String fullName, String idNumber, LocalDate dob, String phoneNumber, 
                              String customerId, CustomerType type, String username, String password) {
        super(fullName, idNumber, dob, phoneNumber, customerId, type);
        this.username = username;
        this.passwordHash = Integer.toHexString(password.hashCode()); // Hash đơn giản demo
        this.registrationDate = new Date();
    }
}