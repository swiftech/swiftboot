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
 * 商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Description("商品订单关系")
@Entity
@Table(name = "DEMO_GOODS_ORDER_REL")
public class GoodsOrderRelEntity extends BaseEntity {

    /**
     * 商品ID 
     */
    @PropertyDescription(value = "商品ID", example = "e8af5ea376fde35fb2c504633f55b128")
    @Column(name = "DEMO_GOODS_ID", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '商品ID'")
    private String goodsId;

    /**
     * 订单ID 
     */
    @PropertyDescription(value = "订单ID", example = "527d36e654f9eaea6a9b46380d253fc9")
    @Column(name = "DEMO_ORDER_ID", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '订单ID'")
    private String orderId;


    public GoodsOrderRelEntity() {
    }

    public GoodsOrderRelEntity(String id) {
        super(id);
    }

    /**
     * 获取商品ID
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品ID
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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


}
