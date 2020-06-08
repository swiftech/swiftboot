package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * Admin user
 *
 * @author swiftech 2019-11-28
 **/
@ApiModel
public class AdminUserSignoutResult extends BasePopulateResult<AdminUserEntity> {

    @ApiModelProperty(value = "Login name of administrator", example = "admin")
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
