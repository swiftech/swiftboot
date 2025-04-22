package org.swiftboot.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * 第三方平台 OAuth 授权
 */
@Entity
@Table(name="PLATFORM_AUTHORIZATION")
public class PlatformAuthorization extends BaseIdEntity {
    @Column(unique = true, nullable = false, length = 64)
    private String openId;
    @Column(unique = true, nullable = false, length = 2048)
    private String accessToken;
    @Column(length = 2048)
    private String refreshToken;
    private Long expiresAt;
    private Long refreshTokenExpiresAt;
    @Column(length = 256)
    private String scope;
    @Column(length = 32)
    private String userId;
    @Column(length = 128)
    private String userName;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Long getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(Long refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
