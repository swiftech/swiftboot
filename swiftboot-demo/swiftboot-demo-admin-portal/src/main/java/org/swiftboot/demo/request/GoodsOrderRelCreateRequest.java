package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.GoodsOrderRelEntity;
import org.swiftboot.web.request.BasePopulateRequest;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 创建商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsOrderRelCreateRequest extends BasePopulateRequest<GoodsOrderRelEntity> {

    @Schema(description = "商品ID",  requiredMode = REQUIRED, example = "e8af5ea376fde35fb2c504633f55b128")
    @JsonProperty("goods_id")
    @Length(max = 32)
    @NotBlank
    private String goodsId;

    /**
     * TODO 此处类型和Entity不一致，参考 GoodsCreateCommand 修改
     */
    @Schema(description = "订单ID",  requiredMode = REQUIRED, example = "527d36e654f9eaea6a9b46380d253fc9")
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
