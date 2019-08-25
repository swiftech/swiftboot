package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderDetailEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BaseListableResult;

import java.util.List;

/**
 * 查询订单明细列表
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderDetailListResult extends BaseListableResult<OrderDetailResult, OrderDetailEntity> {

    @ApiModelProperty("订单明细总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @ApiModelProperty("订单明细列表")
    @JsonProperty("items")
    private
    List<OrderDetailResult> items;

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
    public List<OrderDetailResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<OrderDetailResult> items) {
        this.items = items;
    }
}
