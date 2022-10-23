package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 保存商品结果
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsSaveResult extends BasePopulateResult<GoodsEntity> {

    @ApiModelProperty("商品 ID")
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
