package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.common.auth.request.NamePasswordLoginRequest;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.request.BasePopulateRequest;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * Admin user signin command
 *
 * @author swiftech
 **/
@Schema
public class AdminUserSigninRequest extends NamePasswordLoginRequest {

//    @Schema(description = "Login name of administrator",  requiredMode = REQUIRED, example = "admin")
//    @JsonProperty("login_name")
//    @Length(max = 32)
//    @NotBlank
//    private String loginName;
//
//    @Schema(description = "Login password to login name",  requiredMode = REQUIRED, example = "my_password")
//    @JsonProperty("login_pwd")
//    @Length(max = 64)
//    @NotBlank
//    private String loginPwd;
//
//    /**
//     * Get Login name of administrator
//     *
//     * @return
//     */
//    public String getLoginName() {
//        return loginName;
//    }
//
//    /**
//     * Set Login name of administrator
//     *
//     * @param loginName
//     */
//    public void setLoginName(String loginName) {
//        this.loginName = loginName;
//    }
//
//    /**
//     * Get Login password to login name
//     *
//     * @return
//     */
//    public String getLoginPwd() {
//        return loginPwd;
//    }
//
//    /**
//     * Set Login password to login name
//     *
//     * @param loginPwd
//     */
//    public void setLoginPwd(String loginPwd) {
//        this.loginPwd = loginPwd;
//    }

}
