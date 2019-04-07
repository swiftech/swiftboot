package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 保存订单结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderSaveResult extends BasePopulateResult<OrderEntity> {

    @ApiModelProperty("订单 ID")
    @JsonProperty("order_id")
    private String orderId;

    public OrderSaveResult() {
    }

    public OrderSaveResult(String orderId) {
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
