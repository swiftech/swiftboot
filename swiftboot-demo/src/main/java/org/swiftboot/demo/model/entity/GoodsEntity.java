package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.web.annotation.PropertyDescription;
import org.swiftboot.web.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 *
 * @author swiftech 2019-04-07
 **/
@Description("商品")
@Entity
@Table(name = "DEMO_GOODS")
public class GoodsEntity extends BaseEntity {

    /**
     * 商品名称 
     */
    @PropertyDescription(value = "商品名称", example = "闲趣清闲薄脆饼干")
    @Column(name = "NAME", length = 16, columnDefinition = "VARCHAR(16) COMMENT '商品名称'")
    private String name;

    /**
     * 商品描述 
     */
    @PropertyDescription(value = "商品描述", example = "清闲不腻，松脆松化")
    @Column(name = "DESCRIPTION", length = 64, columnDefinition = "VARCHAR(64) COMMENT '商品描述'")
    private String description;

    /**
     * 商品价格 
     */
    @PropertyDescription(value = "商品价格", example = "12.5")
    @Column(name = "PRICE", scale = 8, precision = 2)//, columnDefinition = "DOUBLE(8.2) COMMENT '商品价格'")
    private Double price;


    public GoodsEntity() {
    }

    public GoodsEntity(String id) {
        super(id);
    }

    /**
     * 获取商品名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取商品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品价格
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置商品价格
     */
    public void setPrice(Double price) {
        this.price = price;
    }


}
