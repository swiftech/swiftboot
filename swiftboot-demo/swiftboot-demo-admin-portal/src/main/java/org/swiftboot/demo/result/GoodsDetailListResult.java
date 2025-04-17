package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import org.swiftboot.web.result.BasePopulateListResult;

import java.util.List;

/**
 * 查询商品详情列表
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsDetailListResult extends BasePopulateListResult<GoodsDetailResult, GoodsDetailEntity> {

    @Schema(description = "商品详情总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @Schema(description = "商品详情列表")
    @JsonProperty("items")
    private
    List<GoodsDetailResult> items;

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
    public List<GoodsDetailResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<GoodsDetailResult> items) {
        this.items = items;
    }
}
