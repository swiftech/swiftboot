package org.swiftboot.demo.model.entity;

import org.swiftboot.web.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * 订单明细
 *
 * @author Allen 2019-04-17
 **/
@Entity
@Table(name = "DEMO_ORDER_DETAIL")
public class OrderDetailEntity extends BaseEntity {

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    private OrderEntity order;

    @Column
    private String description;

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
