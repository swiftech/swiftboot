package org.swiftboot.demo.controller.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.command.BasePopulateCommand;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 创建商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsOrderRelCreateCommand extends BasePopulateCommand<GoodsOrderRelEntity> {

    @ApiModelProperty(value = "商品ID", required = true, example = "e8af5ea376fde35fb2c504633f55b128")
    @JsonProperty("goods_id")
    @Length(max = 32)
    @NotBlank
    private String goodsId;

    @ApiModelProperty(value = "订单ID", required = true, example = "527d36e654f9eaea6a9b46380d253fc9")
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
