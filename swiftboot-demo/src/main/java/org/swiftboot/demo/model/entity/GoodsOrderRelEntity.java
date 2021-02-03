package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * 商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Description("商品订单关系")
@Entity
@Table(name = "DEMO_GOODS_ORDER_REL")
public class GoodsOrderRelEntity extends BaseEntity {

    /**
     * 订单ID
     */
    @PropertyDescription(value = "订单ID", example = "527d36e654f9eaea6a9b46380d253fc9")
    @Column(name = "ORDER_ID", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '订单ID'")
    private String orderId;

    /**
     * 商品
     */
    @PropertyDescription(value = "商品", example = "41882f64aac9da0dee30a63068a0326a")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID", nullable = false)
    private GoodsEntity goods;


    public GoodsOrderRelEntity() {
    }

    public GoodsOrderRelEntity(String id) {
        super(id);
    }

    /**
     * 获取订单ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取商品
     */
    public GoodsEntity getGoods() {
        return goods;
    }

    /**
     * 设置商品
     */
    public void setGoods(GoodsEntity goods) {
        this.goods = goods;
    }

}
