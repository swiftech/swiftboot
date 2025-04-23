package org.swiftboot.common.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Only for JWT mode
 *
 * @since 3.0
 */
public abstract class BaseRefreshTokenDto extends BaseAuthDto {

    @Schema(description = "Refresh Token, only for JWT mode")
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @Schema(description = "Refresh token expires at time in milliseconds, only for JWT mode")
    @JsonProperty(value = "refresh_token_expires_at")
    private Long refreshTokenExpiresAt;

    public BaseRefreshTokenDto() {
    }

    public BaseRefreshTokenDto(String accessToken, long expiresAt, String refreshToken, long refreshTokenExpiresAt) {
        super(accessToken, expiresAt);
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(Long refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
