package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * 带有明细的订单保存对象
 *
 * @author swiftech
 **/
@Schema
public class OrderWithDetailSaveCommand extends OrderCreateCommand {

    @Schema(description = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @NotBlank
    @Length(min = 32, max = 32)
    @JsonProperty("id")
    private String id;

    @Schema(description = "订单明细")
    @JsonProperty("details")
    private List<OrderDetailSave> orderDetails;

    @Schema(description = "订单支付")
    @JsonProperty("payment")
    private OrderPayment orderPayment;

    private List<String> comments;

    /**
     * 获取 ID
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置 ID
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取订单明细
     *
     * @return
     */
    public List<OrderDetailSave> getOrderDetails() {
        return orderDetails;
    }

    /**
     * 设置订单明细
     *
     * @param orderDetails
     */
    public void setOrderDetails(List<OrderDetailSave> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderPayment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPayment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    /**
     * 订单明细保存对象
     */
    @Schema
    public static class OrderDetailSave extends OrderWithDetailCreateCommand.OrderDetailCreate {

        @Schema(description = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
        @NotBlank
        @Size(min = 32, max = 32)
        @JsonProperty("id")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 订单支付保存对象
     */
    @Schema
    public static class OrderPayment extends OrderWithDetailCreateCommand.OrderPaymentCreate {

    }
}
