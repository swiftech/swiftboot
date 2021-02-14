package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

import static org.swiftboot.demo.command.OrderWithDetailCreateCommand.OrderDetailCreate;
import static org.swiftboot.demo.command.OrderWithDetailCreateCommand.OrderPaymentCreate;

/**
 * 带有明细的订单保存对象
 *
 * @author swiftech
 **/
@ApiModel
public class OrderWithDetailSaveCommand extends OrderCreateCommand {

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @NotBlank
    @Size(min = 32, max = 32)
    @JsonProperty("id")
    private String id;

    @ApiModelProperty(value = "订单明细")
    @JsonProperty("details")
    private List<OrderDetailSave> orderDetails;

    @ApiModelProperty(value = "订单支付")
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
    @ApiModel
    public static class OrderDetailSave extends OrderDetailCreate {

        @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
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
    @ApiModel
    public static class OrderPayment extends OrderPaymentCreate {

    }
}
