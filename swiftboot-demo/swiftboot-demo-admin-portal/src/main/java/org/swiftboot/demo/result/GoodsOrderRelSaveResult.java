package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 保存商品订单关系结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsOrderRelSaveResult extends BasePopulateResult<GoodsOrderRelEntity> {

    @Schema(description = "商品订单关系 ID")
    @JsonProperty("goods_order_rel_id")
    private String goodsOrderRelId;

    public GoodsOrderRelSaveResult() {
    }

    public GoodsOrderRelSaveResult(String goodsOrderRelId) {
        this.goodsOrderRelId = goodsOrderRelId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getGoodsOrderRelId() {
        return goodsOrderRelId;
    }

    /**
     * 设置 ID
     *
     * @param goodsOrderRelId
     */
    public void setGoodsOrderRelId(String goodsOrderRelId) {
        this.goodsOrderRelId = goodsOrderRelId;
    }
}
