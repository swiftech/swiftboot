package org.swiftboot.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.GoodsEntity;
import org.swiftboot.web.dto.BasePopulateDto;

/**
 * 保存商品结果
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsSaveResult extends BasePopulateDto<GoodsEntity> {

    @Schema(description = "商品 ID")
    @JsonProperty("goods_id")
    private String goodsId;

    public GoodsSaveResult() {
    }

    public GoodsSaveResult(String goodsId) {
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
