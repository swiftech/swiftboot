package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotNull;

/**
 * 创建商品详情
 *
 * @author swiftech 2019-08-22
 **/
@ApiModel
public class GoodsDetailCreateCommand extends BasePopulateCommand<GoodsDetailEntity> {

    @ApiModelProperty(value = "商品ID")
    @JsonProperty("goods_id")
    @PopulateIgnore
    private String goodsId;

    @ApiModelProperty(value = "商品图片URI", example = "/image/goods/1029")
    @JsonProperty("image_uri")
    @Length(max = 256)
    private String imageUri;

    @ApiModelProperty(value = "折扣", example = "0.85")
    @JsonProperty("discount")
    @NotNull
    private Double discount;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取商品图片URI
     *
     * @return
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * 设置商品图片URI
     *
     * @param imageUri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 获取折扣
     *
     * @return
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * 设置折扣
     *
     * @param discount
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

}
