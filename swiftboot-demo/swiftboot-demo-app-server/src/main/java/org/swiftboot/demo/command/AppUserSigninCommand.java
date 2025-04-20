package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.AppUserEntity;
import org.swiftboot.web.command.BasePopulateCommand;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;


/**
 * App user signin command
 *
 * @author swiftech 2020-02-05
 **/
@Schema
public class AppUserSigninCommand extends BasePopulateCommand<AppUserEntity> {

    @Schema(description = "Login name of app user",  requiredMode = REQUIRED, example = "13866669999")
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
