package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderDetailEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建订单明细结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderDetailCreateResult extends BasePopulateResult<OrderDetailEntity> {

    @ApiModelProperty(value = "订单明细 ID", example = "")
    @JsonProperty("order_detail_id")
    private String orderDetailId;

    public OrderDetailCreateResult() {
    }

    public OrderDetailCreateResult(String orderDetailId) {
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
