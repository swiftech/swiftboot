package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsEntity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建商品结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsCreateResult extends BasePopulateResult<GoodsEntity> {

    @Schema(description = "商品 ID", example = "")
    @JsonProperty("goods_id")
    private String goodsId;

    public GoodsCreateResult() {
    }

    public GoodsCreateResult(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置 ID
     *
     * @param goodsId
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
