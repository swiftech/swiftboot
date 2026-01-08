package org.swiftboot.common.auth.dto;

import org.swiftboot.common.auth.token.AccessToken;

/**
 * @since 3.0
 */
public abstract class BaseAuthDto {

    /**
     * optional if using Cookie under Session mode
     */
    private AccessToken accessToken;

    public BaseAuthDto() {
    }

    public BaseAuthDto(AccessToken accessToken) {
        this.accessToken = accessToken;
    }
    public AccessToken getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

}
