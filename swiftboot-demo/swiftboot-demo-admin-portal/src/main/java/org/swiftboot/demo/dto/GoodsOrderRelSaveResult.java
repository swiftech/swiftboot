package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.GoodsOrderRelEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 保存商品订单关系结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsOrderRelSaveResult extends BasePopulateDto<GoodsOrderRelEntity> {

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
