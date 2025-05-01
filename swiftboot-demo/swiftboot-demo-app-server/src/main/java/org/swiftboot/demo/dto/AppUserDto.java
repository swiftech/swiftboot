package org.swiftboot.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @since 3.0
 */
@Schema(description = "User DTO")
public class AppUserDto {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "Login Name")
    private String loginName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
