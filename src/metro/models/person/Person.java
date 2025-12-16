package metro.models.person;
import java.time.LocalDate;

public abstract class Person {
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
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
    
    public int getAge() {
        return LocalDate.now().getYear() - dob.getYear();
    }

    public String getFullInfor() {
        return String.format("ID: %s | Name: %s | Phone: %s", idNumber, fullName, phoneNumber);
    }


    
    
}