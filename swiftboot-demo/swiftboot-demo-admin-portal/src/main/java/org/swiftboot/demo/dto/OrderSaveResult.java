package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.OrderEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 保存订单结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class OrderSaveResult extends BasePopulateDto<OrderEntity> {

    @Schema(description = "订单 ID")
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
