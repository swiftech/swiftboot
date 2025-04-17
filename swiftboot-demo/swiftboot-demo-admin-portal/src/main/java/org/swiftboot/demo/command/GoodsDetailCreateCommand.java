package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.command.BasePopulateCommand;

import jakarta.validation.constraints.NotNull;

/**
 * 创建商品详情
 *
 * @author swiftech 2019-08-22
 **/
@Schema
public class GoodsDetailCreateCommand extends BasePopulateCommand<GoodsDetailEntity> {

    @Schema(description = "商品ID")
    @JsonProperty("goods_id")
    @PopulateIgnore
    private String goodsId;

    @Schema(description = "商品图片URI", example = "/image/goods/1029")
    @JsonProperty("image_uri")
    @Length(max = 256)
    private String imageUri;

    @Schema(description = "折扣", example = "0.85")
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
