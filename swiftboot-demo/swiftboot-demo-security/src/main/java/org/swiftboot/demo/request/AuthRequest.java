package org.swiftboot.demo.request;

/**
 * @author swiftech
 */
public class AuthRequest {
    String userName;
    String password;

    public String getUserName() {
        return userName;
    }

    public AuthRequest setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest setPassword(String password) {
        this.password = password;
        return this;
    }
}
