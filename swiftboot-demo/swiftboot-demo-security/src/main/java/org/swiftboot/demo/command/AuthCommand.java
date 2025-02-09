package org.swiftboot.demo.command;

/**
 * @author swiftech
 */
public class AuthCommand {
    String userName;
    String password;

    public String getUserName() {
        return userName;
    }

    public AuthCommand setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthCommand setPassword(String password) {
        this.password = password;
        return this;
    }
}
