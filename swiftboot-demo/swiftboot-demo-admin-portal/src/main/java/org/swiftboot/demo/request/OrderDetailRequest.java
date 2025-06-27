package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.OrderDetailEntity;
import org.swiftboot.web.request.BasePopulateRequest;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 创建订单明细
 *
 * @author swiftech 2019-08-22
 **/
@Schema
public class OrderDetailRequest extends BasePopulateRequest<OrderDetailEntity> {

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
