package org.swiftboot.demo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.GoodsEntity;
import org.swiftboot.web.request.BasePopulateRequest;
import org.swiftboot.web.validate.constraint.DecimalString;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 创建商品
 *
 * @author swiftech 2019-08-22
 **/
@Schema
public class GoodsCreateRequest extends BasePopulateRequest<GoodsEntity> {

    @Schema(description = "商品名称", example = "闲趣清闲薄脆饼干")
    @JsonProperty("name")
    @Length(max = 16)
    private String name;

    @Schema(description = "商品描述", example = "清闲不腻，松脆松化")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;

    @Schema(description = "商品价格", example = "12.5")
    @JsonProperty("price")
    private Double price;

    @Schema(description = "商品重量", example = "35.7")
    @JsonProperty("weight")
    @Length(max = 32)
    @NotNull
    @DecimalString
    private String weight;

    @Schema(description = "生产时间", example = "2020-01-16 00:00:00")
    @JsonProperty("production_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime productionTime;

    @Schema(description = "过期日期", example = "2021-01-16")
    @JsonProperty("expire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expireDate;

    /**
     * 获取商品名称
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 设置商品名称
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取商品描述
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品价格
     *
     * @return
     */
    public Double getPrice() {
        return price;
    }

    /**
     * 设置商品价格
     *
     * @param price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    /**
     * 获取生产时间
     *
     * @return
     */
    public LocalDateTime getProductionTime() {
        return productionTime;
    }

    /**
     * 设置生产时间
     *
     * @param productionTime
     */
    public GoodsCreateRequest setProductionTime(LocalDateTime productionTime) {
        this.productionTime = productionTime;
        return this;
    }

    /**
     * 获取过期日期
     *
     * @return
     */
    public LocalDate getExpireDate() {
        return expireDate;
    }

    /**
     * 设置过期日期
     *
     * @param expireDate
     */
    public GoodsCreateRequest setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }
}
