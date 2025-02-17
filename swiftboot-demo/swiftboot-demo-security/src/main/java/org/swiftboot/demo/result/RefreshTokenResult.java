package org.swiftboot.demo.result;

/**
 * @since 3.0.0
 */
public class RefreshTokenResult {

    private String accessToken;

    public RefreshTokenResult(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
