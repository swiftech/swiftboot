package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.common.auth.dto.BaseRefreshTokenDto;

/**
 * App user sign-in.
 *
 * @author swiftech 2020-02-05
 **/
@Schema
public class AppUserSignInDto extends BaseRefreshTokenDto {

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

}
