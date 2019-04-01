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
 * @author swiftech 2019-01-15
 **/
@ApiModel
public class GoodsOrderRelCreateCommand extends BasePopulateCommand<GoodsOrderRelEntity> {

    @ApiModelProperty(value = "商品ID", required = true, example = "7f46aa44e0724ef9af0319a0769bd091")
    @JsonProperty("demo_goods_id")
    @Length(max = 32)
    @NotBlank
    private String demoGoodsId;

    @ApiModelProperty(value = "订单ID", required = true, example = "ea8e6cc03954c56948680b38d9768666")
    @JsonProperty("demo_order_id")
    @Length(max = 32)
    @NotBlank
    private String demoOrderId;


    /**
     * 获取商品ID
     *
     * @return
     */
    public String getDemoGoodsId() {
        return demoGoodsId;
    }

    /**
     * 设置商品ID
     *
     * @param demoGoodsId
     */
    public void setDemoGoodsId(String demoGoodsId) {
        this.demoGoodsId = demoGoodsId;
    }

    /**
     * 获取订单ID
     *
     * @return
     */
    public String getDemoOrderId() {
        return demoOrderId;
    }

    /**
     * 设置订单ID
     *
     * @param demoOrderId
     */
    public void setDemoOrderId(String demoOrderId) {
        this.demoOrderId = demoOrderId;
    }

}
