package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotBlank;

/**
 * Admin user signin command
 *
 * @author swiftech
 **/
@ApiModel
public class AdminUserSigninCommand extends BasePopulateCommand<AdminUserEntity> {

    @ApiModelProperty(value = "Login name of administrator", required = true, example = "admin")
    @JsonProperty("login_name")
    @Length(max = 32)
    @NotBlank
    private String loginName;

    @ApiModelProperty(value = "Login password to login name", required = true, example = "my_password")
    @JsonProperty("login_pwd")
    @Length(max = 64)
    @NotBlank
    private String loginPwd;

//    @JsonIgnore
//    private String group;

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

//    public String getGroup() {
//        return group;
//    }
//
//    public void setGroup(String group) {
//        this.group = group;
//    }
}
