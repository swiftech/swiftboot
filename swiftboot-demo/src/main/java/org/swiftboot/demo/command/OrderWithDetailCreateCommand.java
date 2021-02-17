package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.entity.OrderDetailEntity;
import org.swiftboot.demo.model.entity.OrderPaymentEntity;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 带有明细的订单创建对象
 *
 * @author swiftech
 */
@ApiModel
public class OrderWithDetailCreateCommand extends OrderCreateCommand {

    @ApiModelProperty(value = "订单明细")
    @JsonProperty("details")
    private List<OrderDetailCreate> orderDetails;

    @ApiModelProperty(value = "订单支付")
    @JsonProperty("payment")
    private OrderPaymentCreate orderPayment;

    /**
     * 获取订单明细
     * @return
     */
    public List<OrderDetailCreate> getOrderDetails() {
        return orderDetails;
    }

    /**
     * 设置订单明细
     * @param orderDetails
     */
    public void setOrderDetails(List<OrderDetailCreate> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderPaymentCreate getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPaymentCreate orderPayment) {
        this.orderPayment = orderPayment;
    }

    /**
     * 订单明细子创建对象
     */
    @ApiModel
    public static class OrderDetailCreate extends BasePopulateCommand<OrderDetailEntity> {

        @ApiModelProperty(value = "明细描述", required = true)
        @JsonProperty("description")
        @Length(max = 512)
        @NotBlank
        private String description;

        /**
         * 获取明细描述
         *
         * @return
         */
        public String getDescription() {
            return description;
        }

        /**
         * 设置明细描述
         *
         * @param description
         */
        public void setDescription(String description) {
            this.description = description;
        }

    }


    /**
     * 订单支付子创建对象
     */
    @ApiModel
    public static class OrderPaymentCreate extends BasePopulateCommand<OrderPaymentEntity> {

        @NotNull
        @JsonProperty("total_price")
        private BigDecimal totalPrice;

        @NotNull
        @JsonProperty("pay_status")
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

}
