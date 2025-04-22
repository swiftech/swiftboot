package org.swiftboot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.common.auth.dto.BaseRefreshTokenDto;
import org.swiftboot.web.result.Result;

/**
 * @author swiftech
 */
@Schema(name="Auth Result", description = "Result of authentication")
public class AuthenticatedDto extends BaseRefreshTokenDto implements Result {

    private String role;

    private String permissions;

    public AuthenticatedDto(String accessToken, long expiresAt, String refreshToken, long refreshTokenExpiresAt) {
        super(accessToken, expiresAt, refreshToken, refreshTokenExpiresAt);
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
