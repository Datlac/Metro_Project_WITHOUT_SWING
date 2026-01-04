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
    
    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	public String getLastLoginIP() {
		return lastLoginIP;
	}

	public void setLastLoginIP(String lastLoginIP) {
		this.lastLoginIP = lastLoginIP;
	}

	public AccountStatus getStatus() {
		return status;
	}

	public void setStatus(AccountStatus status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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