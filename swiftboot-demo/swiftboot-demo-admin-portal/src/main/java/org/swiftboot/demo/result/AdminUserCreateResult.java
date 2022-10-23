package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.AdminUserEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建管理员结果
 *
 * @author swiftech 2020-01-06
 **/
@ApiModel
public class AdminUserCreateResult extends BasePopulateResult<AdminUserEntity> {

    @ApiModelProperty(value = "管理员 ID", example = "")
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
