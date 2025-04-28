package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.auth.controller.BaseAuthenticatedCommand;

import jakarta.validation.constraints.NotNull;

/**
 * 创建订单
 *
 * @author swiftech 2019-08-22
 **/
@Schema
public class OrderCreateCommand extends BaseAuthenticatedCommand<OrderEntity> {

    @Schema(description = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    @Length(max = 16)
    private String orderCode;

    @Schema(description = "订单描述", example = "越快越好")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;

    @Schema(description = "商品总数", example = "5")
    @JsonProperty("total_count")
    @NotNull
    private Integer totalCount;

    @Schema(description = "发货地址", example = "极乐世界102号")
    @JsonProperty("address")
    @Length(max = 64)
    private String address;

    /**
     * 获取订单编号
     *
     * @return
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置订单编号
     *
     * @param orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 获取订单描述
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置订单描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品总数
     *
     * @return
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置商品总数
     *
     * @param totalCount
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取发货地址
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置发货地址
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

}
