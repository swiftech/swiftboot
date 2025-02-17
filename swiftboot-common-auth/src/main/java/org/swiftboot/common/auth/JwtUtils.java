package org.swiftboot.common.auth;

import org.springframework.util.StringUtils;

/**
 * @since 3.0.0
 */
public class JwtUtils {

    /**
     *
     * @param authorizationHeader
     * @return
     */
    public static String extractBearerToken(String authorizationHeader) {
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
