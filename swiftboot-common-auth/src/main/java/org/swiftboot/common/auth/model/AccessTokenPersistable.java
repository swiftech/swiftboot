package org.swiftboot.common.auth.model;

/**
 * @since 3.1
 */
public interface AccessTokenPersistable {


    String getAccessToken();

    void setAccessToken(String accessToken);

    Long getAccessTokenExpiresAt();

    void setAccessTokenExpiresAt(Long expiresAt);

}
