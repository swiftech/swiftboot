package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.swiftboot.demo.model.entity.OrderDetailEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.web.command.BasePopulateCommand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 创建订单明细
 *
 * @author swiftech 2019-08-22
 **/
@Schema
public class OrderDetailCreateCommand extends BasePopulateCommand<OrderDetailEntity> {

    @Schema(description = "明细描述",  requiredMode = REQUIRED)
    @JsonProperty("description")
    @Length(max = 512)
    @NotBlank
    private String description;

    @Schema(description = "订单ID",  requiredMode = REQUIRED, example = "f80dc17d1b744a38a438e47c8d95cbd1")
    @JsonProperty("order_id")
    @Length(max = 32)
    @NotNull
    private String orderId;


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

    /**
     * 获取订单ID
     *
     * @return
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单ID
     *
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

}
