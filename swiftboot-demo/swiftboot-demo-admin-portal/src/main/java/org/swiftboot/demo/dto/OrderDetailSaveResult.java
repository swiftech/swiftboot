package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.OrderDetailEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 保存订单明细结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class OrderDetailSaveResult extends BasePopulateDto<OrderDetailEntity> {

    @Schema(description = "订单明细 ID")
    @JsonProperty("order_detail_id")
    private String orderDetailId;

    public OrderDetailSaveResult() {
    }

    public OrderDetailSaveResult(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getOrderDetailId() {
        return orderDetailId;
    }

    /**
     * 设置 ID
     *
     * @param orderDetailId
     */
    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }
}
