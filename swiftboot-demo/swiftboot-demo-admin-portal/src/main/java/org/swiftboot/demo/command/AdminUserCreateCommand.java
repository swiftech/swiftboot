package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotBlank;

/**
 * 创建管理员
 *
 * @author swiftech 2020-01-06
 **/
@ApiModel
public class AdminUserCreateCommand extends BasePopulateCommand<AdminUserEntity> {

    @ApiModelProperty(value = "登录名", required = true, example = "admin")
    @JsonProperty("login_name")
    @Length(max = 32)
    @NotBlank
    private String loginName;

    @ApiModelProperty(value = "登录密码", required = true, notes = "MD5加盐", example = "a43b66902590c003c213a5ed1b6f92e3")
    @JsonProperty("login_pwd")
    @Length(max = 64)
    @NotBlank
    private String loginPwd;

    @ApiModelProperty(value = "用户姓名", example = "James Bond")
    @JsonProperty("user_name")
    @Length(max = 64)
    private String userName;


    /**
     * 获取登录名
     *
     * @return
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * 设置登录名
     *
     * @param loginName
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * 获取登录密码
     *
     * @return
     */
    public String getLoginPwd() {
        return loginPwd;
    }

    /**
     * 设置登录密码
     *
     * @param loginPwd
     */
    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    /**
     * 获取用户姓名
     *
     * @return
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户姓名
     *
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
