package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.GoodsEntity;
import org.swiftboot.web.dto.BasePopulateListDto;

import java.util.List;

/**
 * 查询商品列表
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsListResult extends BasePopulateListDto<GoodsResult, GoodsEntity> {

    @Schema(description = "商品总数（用于分页查询）")
    @JsonProperty("total")
    private long total;

    @Schema(description = "商品列表")
    @JsonProperty("items")
    private
    List<GoodsResult> items;

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
    public List<GoodsResult> getItems() {
        return items;
    }

    /**
     * 设置查询结果集
     *
     * @param items
     */
    @Override
    public void setItems(List<GoodsResult> items) {
        this.items = items;
    }
}
