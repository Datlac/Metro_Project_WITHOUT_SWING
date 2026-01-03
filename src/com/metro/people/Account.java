package com.metro.people;

import com.metro.enums.AccountStatus; // Cần tạo Enum này nếu chưa có

public class Account {
    private String email;
    private boolean isEmailVerified;
    private String lastLoginIP;
    private AccountStatus status;
    private String password; // Lưu ý bảo mật thực tế cần hash

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        this.status = AccountStatus.ACTIVE; // Giả sử đã có Enum ACTIVE
        this.isEmailVerified = false;
    }

    public boolean changePassword(String oldPass, String newPass) {
        if (this.password.equals(oldPass)) {
            this.password = newPass;
            return true;
        }
        return false;
    }

    public void recoverAccount() {
        System.out.println("Recovery email sent to " + email);
    }
}