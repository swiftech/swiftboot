package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.AdminUserEntity;
import org.swiftboot.web.dto.BasePopulateListDto;

import java.util.List;

/**
 * 查询管理员列表
 *
 * @author swiftech 2020-01-06
 **/
@Schema
public class AdminUserListResult extends BasePopulateListDto<AdminUserResult, AdminUserEntity> {

    @Schema(description = "管理员总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @Schema(description = "管理员列表")
    @JsonProperty("items")
    private
    List<AdminUserResult> items;

    /**
     * 获取总数（用于分页查询）
     *
     * @return
     */
    public long getTotal() {
        return total;
    }

    /**
     * 设置总数（用于分页查询）
     *
     * @param total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 获取查询结果集
     *
     * @return
     */
    @Override
    public List<AdminUserResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<AdminUserResult> items) {
        this.items = items;
    }
}
