package org.swiftboot.common.auth.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;


/**
 * Request for user login with name and password.
 *
 * @author
 * @since 3.0
 **/
@Schema(description = "Login by Name and Password")
public class NamePasswordLoginRequest {

    @Schema(description = "Login name of user", requiredMode = REQUIRED, example = "13066669999")
    @JsonProperty("login_name")
    @Length(max = 32)
    @NotBlank
    private String loginName;

    @Schema(description = "Password for login name", requiredMode = REQUIRED, example = "my_password")
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
