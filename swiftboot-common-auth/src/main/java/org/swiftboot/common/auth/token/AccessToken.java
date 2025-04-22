package org.swiftboot.common.auth.token;

/**
 *
 * @param tokenValue
 * @param expiresAt
 */
public record AccessToken(String tokenValue, Long expiresAt) {
}
