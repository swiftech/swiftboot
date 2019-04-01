package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.web.annotation.PropertyDescription;
import org.swiftboot.web.model.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品订单关系
 *
 * @author swiftech 2019-01-15
 **/
@Description("商品订单关系")
@Entity
@Table(name = "DEMO_GOODS_ORDER_REL")
public class GoodsOrderRelEntity extends BaseEntity {

    /**
     * 商品ID 
     */
    @PropertyDescription(value = "商品ID", example = "7f46aa44e0724ef9af0319a0769bd091")
    @Column(name = "DEMO_GOODS_ID", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '商品ID'")
    private String demoGoodsId;

    /**
     * 订单ID 
     */
    @PropertyDescription(value = "订单ID", example = "ea8e6cc03954c56948680b38d9768666")
    @Column(name = "DEMO_ORDER_ID", length = 32, nullable = false, columnDefinition = "CHAR(32) NOT NULL COMMENT '订单ID'")
    private String demoOrderId;


    /**
     * 获取商品ID
     */
    public String getDemoGoodsId() {
        return demoGoodsId;
    }

    /**
     * 设置商品ID
     */
    public void setDemoGoodsId(String demoGoodsId) {
        this.demoGoodsId = demoGoodsId;
    }

    /**
     * 获取订单ID
     */
    public String getDemoOrderId() {
        return demoOrderId;
    }

    /**
     * 设置订单ID
     */
    public void setDemoOrderId(String demoOrderId) {
        this.demoOrderId = demoOrderId;
    }


}
