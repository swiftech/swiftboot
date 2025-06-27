package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.request.BasePopulateRequest;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 创建管理员
 * 
 * @author swiftech 2020-01-06
 **/
@Schema
public class AdminUserRequest extends BasePopulateRequest<AdminUserEntity> {

    @Schema(description = "登录名",  requiredMode = REQUIRED, example = "admin")
    @JsonProperty("login_name")
    @Length(max = 32)
    @NotBlank
    private String loginName;

    @Schema(description = "登录密码(MD5加盐)",  requiredMode = REQUIRED, example = "a43b66902590c003c213a5ed1b6f92e3")
    @JsonProperty("login_pwd")
    @Length(max = 64)
    @NotBlank
    private String loginPwd;

    @Schema(description = "用户姓名", example = "James Bond")
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
