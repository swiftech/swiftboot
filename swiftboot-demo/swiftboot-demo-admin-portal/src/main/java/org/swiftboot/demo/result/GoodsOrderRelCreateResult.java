package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建商品订单关系结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsOrderRelCreateResult extends BasePopulateResult<GoodsOrderRelEntity> {

    @ApiModelProperty(value = "商品订单关系 ID", example = "")
    @JsonProperty("goods_order_rel_id")
    private String goodsOrderRelId;

    public GoodsOrderRelCreateResult() {
    }

    public GoodsOrderRelCreateResult(String goodsOrderRelId) {
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
