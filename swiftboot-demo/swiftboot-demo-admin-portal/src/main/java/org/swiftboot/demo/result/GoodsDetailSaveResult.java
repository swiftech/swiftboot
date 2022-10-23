package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 保存商品详情结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsDetailSaveResult extends BasePopulateResult<GoodsDetailEntity> {

    @ApiModelProperty("商品详情 ID")
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
