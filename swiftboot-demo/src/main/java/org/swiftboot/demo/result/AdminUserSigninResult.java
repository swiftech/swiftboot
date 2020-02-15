package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * Admin user
 *
 * @author Allen 2019-11-28
 **/
@ApiModel
public class AdminUserSigninResult extends BasePopulateResult<AdminUserEntity> {

    @ApiModelProperty(value = "Login name of administrator", example = "admin")
    @JsonProperty("login_name")
    private String loginName;

    @ApiModelProperty(value = "Updating time", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @ApiModelProperty(value = "Entity ID", example = "basident20191119010450544siobnic")
    @JsonProperty("id")
    private String id;

    @ApiModelProperty(value = "Sign in success", example = "true")
    private boolean success = false;

    @ApiModelProperty(value = "Token for admin user", example = "772eb2add9b64e40972468c779b3b952")
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
