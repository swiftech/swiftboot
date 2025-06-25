package org.swiftboot.demo.model;

import jakarta.persistence.*;
import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseBoolDeleteEntity;

import java.util.List;
import java.util.Set;

/**
 * 订单
 *
 * @author swiftech 2019-04-07
 * @see OrderDetailEntity
 **/
@Description("订单")
@Entity
@Table(name = "DEMO_ORDER")
public class OrderEntity extends BaseBoolDeleteEntity {

    /**
     * 订单编号
     */
    @PropertyDescription(value = "订单编号", example = "2019032411081201")
    @Column(name = "ORDER_CODE", length = 16)
    private String orderCode;

    /**
     * 订单描述
     */
    @PropertyDescription(value = "订单描述", example = "越快越好")
    @Column(name = "DESCRIPTION", length = 64)
    private String description;

    /**
     * 商品总数
     */
    @PropertyDescription(value = "商品总数", example = "5")
    @Column(name = "TOTAL_COUNT")
    private Integer totalCount = 0;

    /**
     * 发货地址
     */
    @PropertyDescription(value = "发货地址", example = "极乐世界102号")
    @Column(name = "ADDRESS", length = 64)
    private String address;

    /**
     * APP_USER ID
     */
    @PropertyDescription(value = "APP_USER ID", example = "appuser201912030118069998d7e8179")
    @Column(name = "USER_ID", length = 32, nullable = false)
    private String userId;

    /**
     * 订单明细
     */
    @PropertyDescription(value = "订单明细")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
    private Set<OrderDetailEntity> orderDetails;

    @PropertyDescription(value = "订单支付")
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "ORDER_PAYMENT_ID", nullable = true)
    private OrderPaymentEntity orderPayment;

    @Transient
    private List<String> comments;


    public OrderEntity() {
    }

    public OrderEntity(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id='" + getId() + "', " +
                "description='" + description + '\'' +
                '}';
    }

    /**
     * 获取订单编号
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置订单编号
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 获取订单描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置订单描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品总数
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置商品总数
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取发货地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置发货地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取APP_USER ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置APP_USER ID
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取订单明细
     */
    public Set<OrderDetailEntity> getOrderDetails() {
        return orderDetails;
    }

    /**
     * 设置订单明细
     */
    public void setOrderDetails(Set<OrderDetailEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderPaymentEntity getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPaymentEntity orderPayment) {
        this.orderPayment = orderPayment;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
