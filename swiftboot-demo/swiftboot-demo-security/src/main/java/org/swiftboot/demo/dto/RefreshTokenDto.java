package org.swiftboot.demo.dto;

import org.swiftboot.common.auth.dto.BaseAuthDto;
import org.swiftboot.common.auth.token.RefreshToken;

/**
 * @since 3.0.0
 */
public class RefreshTokenDto extends BaseAuthDto {

    public RefreshTokenDto() {
    }

    public RefreshTokenDto(String accessToken, long expiresAt) {
        super(accessToken, expiresAt);
    }

    public RefreshTokenDto(RefreshToken refreshToken) {
        super(refreshToken.tokenValue(), refreshToken.expiresAt());
    }
}
