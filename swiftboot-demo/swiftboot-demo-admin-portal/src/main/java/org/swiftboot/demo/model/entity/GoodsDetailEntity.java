package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 商品详情
 *
 * @author swiftech 2019-08-23
 **/
@Description("商品详情")
@Entity
@Table(name = "DEMO_GOODS_DETAIL")
public class GoodsDetailEntity extends BaseEntity {

    /**
     * 商品图片URI
     */
    @PropertyDescription(value = "商品图片URI", example = "/image/goods/1029")
    @Column(name = "IMAGE_URI", length = 256)
    private String imageUri;

    /**
     * 折扣
     */
    @PropertyDescription(value = "折扣", example = "0.85")
    @Column(name = "DISCOUNT", scale = 8, precision = 2)//, columnDefinition = "DOUBLE(8,2) COMMENT '折扣'")
    private Double discount;


    public GoodsDetailEntity() {
    }

    public GoodsDetailEntity(String id) {
        super(id);
    }

    /**
     * 获取商品图片URI
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * 设置商品图片URI
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 获取折扣
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * 设置折扣
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

}
