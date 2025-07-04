package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 创建管理员结果
 *
 * @author swiftech 2020-01-06
 **/
@Schema
public class AdminUserCreateResult extends BasePopulateDto<AdminUserEntity> {

    @Schema(description = "管理员 ID", example = "")
    @JsonProperty("admin_user_id")
    private String adminUserId;

    public AdminUserCreateResult() {
    }

    public AdminUserCreateResult(String adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getAdminUserId() {
        return adminUserId;
    }

    /**
     * 设置 ID
     *
     * @param adminUserId
     */
    public void setAdminUserId(String adminUserId) {
        this.adminUserId = adminUserId;
    }
}
