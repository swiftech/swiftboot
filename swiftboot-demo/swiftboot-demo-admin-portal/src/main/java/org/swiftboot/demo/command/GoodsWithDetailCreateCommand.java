package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotNull;

/**
 * 创建商品
 *
 * @author swiftech 2019-08-22
 **/
@ApiModel
public class GoodsWithDetailCreateCommand extends BasePopulateCommand<GoodsEntity> {

    @ApiModelProperty(value = "商品名称", example = "闲趣清闲薄脆饼干")
    @JsonProperty("name")
    @Length(max = 16)
    private String name;

    @ApiModelProperty(value = "商品描述", example = "清闲不腻，松脆松化")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;

    @ApiModelProperty(value = "商品价格", example = "12.5")
    @JsonProperty("price")
    @NotNull
    private Double price;

    @ApiModelProperty(value = "商品详情")
    @JsonProperty("goods_detail")
    @NotNull
    private GoodsDetailCreateCommand goodsDetail;

    /**
     * 获取商品名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取商品描述
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品价格
     *
     * @return
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置商品价格
     *
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    public GoodsDetailCreateCommand getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(GoodsDetailCreateCommand goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
