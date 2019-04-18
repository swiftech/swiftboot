package org.swiftboot.demo.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.entity.OrderEntity;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotNull;

/**
 * 创建订单
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderCreateCommand extends BasePopulateCommand<OrderEntity> {

    @ApiModelProperty(value = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    @Length(max = 16)
    private String orderCode;

    @ApiModelProperty(value = "订单描述", example = "越快越好")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;

    @ApiModelProperty(value = "商品总数", example = "5")
    @JsonProperty("total_count")
    @NotNull
    private Integer totalCount;

    @ApiModelProperty(value = "发货地址", example = "极乐世界102号")
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
