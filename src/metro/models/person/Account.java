package metro.models.person;
import metro.enums.AccountStatus;

public class Account {
    private String email;
    private boolean isEmailVerified;
    private String lastLoginIP;
    private AccountStatus status;

    public Account(String email) {
        this.email = email;
        this.status = AccountStatus.ACTIVE;
        this.isEmailVerified = false;
    }

    public boolean changePassword(String oldPass, String newPass) {
        // Logic đổi pass
        return true;
    }

    public void recoverAccount() {
        System.out.println("Gửi email khôi phục tới: " + email);
    }
}