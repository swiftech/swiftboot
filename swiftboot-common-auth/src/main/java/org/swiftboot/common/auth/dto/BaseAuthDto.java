package org.swiftboot.common.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @since 3.0
 */
public abstract class BaseAuthDto {

    /**
     * optional if using Cookie under Session mode
     */
    @Schema(description = "Access Token", example = "6d567f2dfe72946ef5f45a61d799348b6d281c1d66bf5cad83aef50964bbd84dcc4c51f7adc638fd17eec68b300c73a783475fd3eeb35c88c566760993ffd906d43cb3f9a7126f7fbd66a56da345209c7816bb9de2f0ae57201394c21e45b01bebb27ec7abcdc65a374fe6cf35ae37c49c5017e5e04f57ae8d9e8620bac30cf5")
    @JsonProperty(value = "access_token")
    private String accessToken;

    @Schema(description = "Access token expires at time in milliseconds", example = "1747885080")
    @JsonProperty(value = "expires_at")
    private Long expiresAt;

    public BaseAuthDto() {
    }

    public BaseAuthDto(String accessToken, long expiresAt) {
        this.accessToken = accessToken;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }
}
