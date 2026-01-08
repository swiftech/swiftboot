package org.swiftboot.common.auth.dto;

import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.token.RefreshToken;

/**
 * Only for JWT mode
 *
 * @since 3.0
 */
public abstract class BaseRefreshTokenDto extends BaseAuthDto {

    private RefreshToken refreshToken;

    public BaseRefreshTokenDto() {
    }

    public BaseRefreshTokenDto(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public BaseRefreshTokenDto(AccessToken accessToken, RefreshToken refreshToken) {
        super(accessToken);
        this.refreshToken = refreshToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

}
