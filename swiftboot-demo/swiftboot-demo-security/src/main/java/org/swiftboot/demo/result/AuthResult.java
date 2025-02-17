package org.swiftboot.demo.result;

import org.swiftboot.web.result.Result;

/**
 * @author swiftech
 */
public class AuthResult implements Result {

    private String accessToken;

    private String refreshToken;

    private String role;

    private String permissions;

    public AuthResult() {
    }

    public AuthResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public AuthResult(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
