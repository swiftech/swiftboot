package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.web.command.BasePopulateCommand;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;

/**
 * 创建商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsOrderRelCreateCommand extends BasePopulateCommand<GoodsOrderRelEntity> {

    @Schema(description = "商品ID", required = true, example = "e8af5ea376fde35fb2c504633f55b128")
    @JsonProperty("goods_id")
    @Length(max = 32)
    @NotBlank
    private String goodsId;

    /**
     * TODO 此处类型和Entity不一致，参考 GoodsCreateCommand 修改
     */
    @Schema(description = "订单ID", required = true, example = "527d36e654f9eaea6a9b46380d253fc9")
    @JsonProperty("order_id")
    @Length(max = 32)
    @NotBlank
    private String orderId;


    /**
     * 获取商品ID
     *
     * @return
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品ID
     *
     * @param goodsId
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取订单ID
     *
     * @return
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
