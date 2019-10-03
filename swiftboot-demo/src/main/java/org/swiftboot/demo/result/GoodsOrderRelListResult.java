package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import org.swiftboot.web.result.BasePopulateListResult;

import java.util.List;

/**
 * 查询商品订单关系列表
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsOrderRelListResult extends BasePopulateListResult<GoodsOrderRelResult, GoodsOrderRelEntity> {

    @ApiModelProperty("商品订单关系总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @ApiModelProperty("商品订单关系列表")
    @JsonProperty("items")
    private
    List<GoodsOrderRelResult> items;

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
    public List<GoodsOrderRelResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<GoodsOrderRelResult> items) {
        this.items = items;
    }
}
