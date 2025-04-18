package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.command.BasePopulateCommand;

import jakarta.validation.constraints.NotBlank;

/**
 * Admin user signin command
 *
 * @author swiftech
 **/
@Schema
public class AdminUserSigninCommand extends BasePopulateCommand<AdminUserEntity> {

    @Schema(description = "Login name of administrator",  requiredMode = REQUIRED, example = "admin")
    @JsonProperty("login_name")
    @Length(max = 32)
    @NotBlank
    private String loginName;

    @Schema(description = "Login password to login name",  requiredMode = REQUIRED, example = "my_password")
    @JsonProperty("login_pwd")
    @Length(max = 64)
    @NotBlank
    private String loginPwd;

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
     * Get Login password to login name
     *
     * @return
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * Set Login password to login name
     *
     * @param loginPwd
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

}
