package org.swiftboot.common.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request that refresh access token.
 *
 * @since 3.0
 */
@Schema(description = "Refresh Access Token")
public class RefreshTokenRequest {

    @Schema(description = "Refresh token", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
