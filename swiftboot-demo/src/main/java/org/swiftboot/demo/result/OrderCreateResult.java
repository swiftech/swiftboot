package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建订单结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderCreateResult extends BasePopulateResult<OrderEntity> {

    @ApiModelProperty(value = "订单 ID", example = "")
    @JsonProperty("order_id")
    private String orderId;

    public OrderCreateResult() {
    }

    public OrderCreateResult(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置 ID
     *
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
