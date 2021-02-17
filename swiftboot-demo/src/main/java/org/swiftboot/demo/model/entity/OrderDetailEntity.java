package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * 订单明细
 *
 * @author swiftech 2019-08-25
 **/
@Description("订单明细")
@Entity
@Table(name = "DEMO_ORDER_DETAIL")
public class OrderDetailEntity extends BaseEntity {

    /**
     * 明细描述
     */
    @PropertyDescription(value = "明细描述")
    @Column(name = "DESCRIPTION", length = 512, nullable = false, columnDefinition = "VARCHAR(512) NOT NULL COMMENT '明细描述'")
    private String description;

    /**
     * 订单
     */
    @PropertyDescription(value = "订单")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private OrderEntity order;


    public OrderDetailEntity() {
    }

    public OrderDetailEntity(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return "OrderDetailEntity{" +
                "description='" + description + '\'' +
                '}';
    }

    /**
     * 获取明细描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置明细描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取订单
     */
    public OrderEntity getOrder() {
        return order;
    }

    /**
     * 设置订单
     */
    public void setOrder(OrderEntity order) {
        this.order = order;
    }

}
