package org.swiftboot.common.auth.token;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 *
 */
public final class RefreshToken {
    @Schema(description = "Refresh Token, only for JWT mode", example = "5a81b20361e335bdfa30ee0b35b4a0f8dc921bda72b1dccf0866a694ad0b58fc76996816d9c8f9069dd1d77e0323420ed3ee4a59893c74df5e9701d130d81694882d0d84d454928ac8638b036dd8785891d59baf0fb9afed2fbcedee158d02ce69d67e447a999492ca99172987184d0e42aaae7451920431ade3a1814ad6090b")
    private final String tokenValue;

    @Schema(description = "Refresh token expires at time in milliseconds, only works for JWT mode", example = "1747885080")
    private final Long expiresAt;

    /**
     * @param tokenValue
     * @param expiresAt
     */
    public RefreshToken(String tokenValue, Long expiresAt) {
        this.tokenValue = tokenValue;
        this.expiresAt = expiresAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(expiresAt, that.expiresAt) && Objects.equals(tokenValue, that.tokenValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenValue, expiresAt);
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    @Override
    public String toString() {
        return "RefreshToken[" +
                "tokenValue=" + tokenValue + ", " +
                "expiresAt=" + expiresAt + ']';
    }

}
