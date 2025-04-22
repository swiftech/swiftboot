package org.swiftboot.common.auth.token;

import java.util.Objects;

/**
 *
 * @param tokenValue
 * @param expiresAt
 */
public record RefreshToken(String tokenValue, Long expiresAt) {

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
}
