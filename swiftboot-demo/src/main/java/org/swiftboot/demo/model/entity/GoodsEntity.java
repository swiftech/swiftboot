package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

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

    @PropertyDescription(value = "生产时间", example = "2020-01-16 00:00:00")
    @Column(name = "PRODUCTION_TIME", columnDefinition = "TIMESTAMP COMMENT '生产时间'")
    private LocalDateTime productionTime;

    @PropertyDescription(value = "过期日期", example = "2021-01-16")
    @Column(name = "EXPIRE_DATE", columnDefinition = "DATE COMMENT '过期日期'")
    private LocalDate expireDate;

    /**
     * 商品详情
     */
    @PropertyDescription(value = "商品详情")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "GOODS_DETAIL_ID", nullable = true)
    private GoodsDetailEntity goodsDetail;

    /**
     * 商品订单关系
     */
    @PropertyDescription(value = "商品订单关系")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "goods")
    private Set<GoodsOrderRelEntity> goodsOrderRels;


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

    public LocalDateTime getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(LocalDateTime productionTime) {
        this.productionTime = productionTime;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public GoodsEntity setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }

    /**
     * 获取商品详情
     */
    public GoodsDetailEntity getGoodsDetail() {
        return goodsDetail;
    }

    /**
     * 设置商品详情
     */
    public void setGoodsDetail(GoodsDetailEntity goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    /**
     * 获取商品订单关系
     */
    public Set<GoodsOrderRelEntity> getGoodsOrderRels() {
        return goodsOrderRels;
    }

    /**
     * 设置商品订单关系
     */
    public void setGoodsOrderRels(Set<GoodsOrderRelEntity> goodsOrderRels) {
        this.goodsOrderRels = goodsOrderRels;
    }

}
