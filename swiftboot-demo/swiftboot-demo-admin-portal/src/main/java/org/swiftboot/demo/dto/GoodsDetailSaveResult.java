package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.GoodsDetailEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 保存商品详情结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsDetailSaveResult extends BasePopulateDto<GoodsDetailEntity> {

    @Schema(description = "商品详情 ID")
    @JsonProperty("goods_detail_id")
    private String goodsDetailId;

    public GoodsDetailSaveResult() {
    }

    public GoodsDetailSaveResult(String goodsDetailId) {
        this.goodsDetailId = goodsDetailId;
    }

    /**
     * 获取 ID
     *
     * @return
     */
    public String getGoodsDetailId() {
        return goodsDetailId;
    }

    /**
     * 设置 ID
     *
     * @param goodsDetailId
     */
    public void setGoodsDetailId(String goodsDetailId) {
        this.goodsDetailId = goodsDetailId;
    }
}
