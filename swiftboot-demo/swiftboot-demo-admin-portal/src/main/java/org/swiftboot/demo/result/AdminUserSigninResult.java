package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateResult;

/**
 * Admin user
 *
 * @author swiftech 2019-11-28
 **/
@Schema
public class AdminUserSigninResult extends BasePopulateResult<AdminUserEntity> {

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

    @Schema(description = "Token for admin user", example = "772eb2add9b64e40972468c779b3b952")
    @JsonProperty("swiftboot_shiro_token")
    private String token;


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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
