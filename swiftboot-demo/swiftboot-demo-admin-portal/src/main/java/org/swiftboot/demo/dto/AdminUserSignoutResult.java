package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * Admin user
 *
 * @author swiftech 2019-11-28
 **/
@Schema
public class AdminUserSignoutResult extends BasePopulateDto<AdminUserEntity> {

    @Schema(description = "Login name of administrator", example = "admin")
    @JsonProperty("login_name")
    private String loginName;

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

}
