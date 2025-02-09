package org.swiftboot.demo.result;

import org.swiftboot.web.result.Result;

/**
 * @author swiftech
 */
public class AuthResult implements Result {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
