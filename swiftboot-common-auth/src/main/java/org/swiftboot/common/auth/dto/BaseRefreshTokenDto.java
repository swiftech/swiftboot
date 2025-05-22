package org.swiftboot.common.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Only for JWT mode
 *
 * @since 3.0
 */
public abstract class BaseRefreshTokenDto extends BaseAuthDto {

    @Schema(description = "Refresh Token, only for JWT mode", example = "5a81b20361e335bdfa30ee0b35b4a0f8dc921bda72b1dccf0866a694ad0b58fc76996816d9c8f9069dd1d77e0323420ed3ee4a59893c74df5e9701d130d81694882d0d84d454928ac8638b036dd8785891d59baf0fb9afed2fbcedee158d02ce69d67e447a999492ca99172987184d0e42aaae7451920431ade3a1814ad6090b")
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    @Schema(description = "Refresh token expires at time in milliseconds, only works for JWT mode", example = "1747885080")
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
