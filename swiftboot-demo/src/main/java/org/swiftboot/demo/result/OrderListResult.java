package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BaseListableResult;

import java.util.List;

/**
 * 查询订单列表
 *
 * @author swiftech 2019-01-15
 **/
@ApiModel
public class OrderListResult extends BaseListableResult<OrderResult, OrderEntity> {

    @ApiModelProperty("订单总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @ApiModelProperty("订单列表")
    @JsonProperty("items")
    private
    List<OrderResult> items;

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
    public List<OrderResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<OrderResult> items) {
        this.items = items;
    }
}
