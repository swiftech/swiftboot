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
    @Schema(description = "Access Token", example = "772eb2add9b64e40972468c779b3b952")
    @JsonProperty(value = "access_token")
    private String accessToken;

    @Schema(description = "Access token expires at time in milliseconds")
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
