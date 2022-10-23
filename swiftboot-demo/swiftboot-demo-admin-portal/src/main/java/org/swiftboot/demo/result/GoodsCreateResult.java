package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsEntity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 创建商品结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsCreateResult extends BasePopulateResult<GoodsEntity> {

    @ApiModelProperty(value = "商品 ID", example = "")
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
