package org.swiftboot.common.auth.token;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 *
 */
public final class AccessToken {

    @Schema(description = "Access Token", example = "6d567f2dfe72946ef5f45a61d799348b6d281c1d66bf5cad83aef50964bbd84dcc4c51f7adc638fd17eec68b300c73a783475fd3eeb35c88c566760993ffd906d43cb3f9a7126f7fbd66a56da345209c7816bb9de2f0ae57201394c21e45b01bebb27ec7abcdc65a374fe6cf35ae37c49c5017e5e04f57ae8d9e8620bac30cf5")
    private final String tokenValue;

    @Schema(description = "Access token expires at time in milliseconds", example = "1747885080")
    private final Long expiresAt;

    /**
     * @param tokenValue
     * @param expiresAt
     */
    public AccessToken(String tokenValue, Long expiresAt) {
        this.tokenValue = tokenValue;
        this.expiresAt = expiresAt;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (AccessToken) obj;
        return Objects.equals(this.tokenValue, that.tokenValue) &&
                Objects.equals(this.expiresAt, that.expiresAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenValue, expiresAt);
    }

    @Override
    public String toString() {
        return "AccessToken[" +
                "tokenValue=" + tokenValue + ", " +
                "expiresAt=" + expiresAt + ']';
    }

}
