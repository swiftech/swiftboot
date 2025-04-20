package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * App user sign-in.
 *
 * @author swiftech 2020-02-05
 **/
@Schema
public class AppUserSignInDto extends BasePopulateResult<AppUserEntity> {

    @Schema(description = "User ID", example = "basident20191119010450544siobnic")
    @JsonProperty("id")
    private String id;

    @Schema(description = "Login name of app user", example = "13866669999")
    @JsonProperty("login_name")
    private String loginName;

    @Schema(description = "Updating time", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @Schema(description = "Sign in success", example = "true")
    private boolean success = true;

    @Schema(description = "Access Token for app user, optional if using Cookie", example = "772eb2add9b64e40972468c779b3b952")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "When the access token expires")
    private Long expiresAt;

    @Schema(description = "Refresh token for refreshing access token, only for JWT mode")
    private String refreshToken;

    @Schema(description = "When the refresh token expires, only for JWT mode")
    private Long refreshTokenExpiresAt;

    /**
     * Get Login name of app user
     *
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Set Login name of app user
     *
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * Get Updating time
     *
     * @return
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * Set Updating time
     *
     * @param updateTime
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Get Entity ID
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * Set Entity ID
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenExpiresAt() {
        return refreshTokenExpiresAt;
    }

    public void setRefreshTokenExpiresAt(Long refreshTokenExpiresAt) {
        this.refreshTokenExpiresAt = refreshTokenExpiresAt;
    }
}
