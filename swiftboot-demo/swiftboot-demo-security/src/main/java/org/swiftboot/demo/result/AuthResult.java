package org.swiftboot.demo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.result.Result;

/**
 * @author swiftech
 */
@Schema(name="Auth Result", description = "Result of authentication")
public class AuthResult implements Result {

    @Schema(description = "Access Token")
    private String accessToken;

    @Schema(description = "Refresh Token")
    private String refreshToken;

    @Schema(description = "Access token expires at time in milliseconds")
    private long expiresAt;

    @Schema(description = "Refresh token expires at time in milliseconds")
    private long refreshTokenExpiresAt;

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

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public long getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(long refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
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
