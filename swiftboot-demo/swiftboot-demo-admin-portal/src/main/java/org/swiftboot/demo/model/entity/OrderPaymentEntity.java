package org.swiftboot.demo.model.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.model.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * @author allen
 */
@Description("订单支付")
@Entity
@Table(name = "DEMO_ORDER_PAYMENT")
public class OrderPaymentEntity extends BaseEntity {

    @Column(name = "TOTAL_PRICE")
    private BigDecimal totalPrice;

    @Column(name = "PAY_STATUS")
    private int payStatus;

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }
}
