package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * Admin user
 *
 * @author swiftech 2019-11-28
 **/
@Schema
public class AdminUserSigninResult extends BasePopulateDto<AdminUserEntity> {

    @Schema(description = "Login name of administrator", example = "admin")
    @JsonProperty("login_name")
    private String loginName;

    @Schema(description = "Updating time", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @Schema(description = "Entity ID", example = "basident20191119010450544siobnic")
    @JsonProperty("id")
    private String id;

    @Schema(description = "Sign in success", example = "true")
    private boolean success = false;

    @Schema(description = "Access token for admin user", example = "772eb2add9b64e40972468c779b3b952")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Access token expires at a specific time in milliseconds")
    @JsonProperty("expires_at")
    private Long expiresAt;


    /**
     * Get Login name of administrator
     *
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Set Login name of administrator
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
}
