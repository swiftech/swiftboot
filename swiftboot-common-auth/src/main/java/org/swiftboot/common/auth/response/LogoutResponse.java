package org.swiftboot.common.auth.response;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.swiftboot.common.auth.aop.JwtLogoutResponseAdvice;
import org.swiftboot.web.response.Response;

/**
 * Return the response to client in controller method,
 * the refresh token(JWT mode) or user session(Session mode) will be cleared automatically.
 *
 * @param <R> Type of DTO object returns to client.
 * @see JwtLogoutResponseAdvice
 * @since 3.0
 */
public class LogoutResponse<R> extends Response<R> {

    @JsonIgnore
    private String accessToken;

    public LogoutResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public LogoutResponse(R result, String accessToken) {
        super(result);
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
