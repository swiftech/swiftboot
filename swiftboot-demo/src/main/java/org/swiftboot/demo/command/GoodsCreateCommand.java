package org.swiftboot.demo.command;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.swiftboot.demo.model.entity.GoodsEntity;
import org.swiftboot.web.command.BasePopulateCommand;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 创建商品
 *
 * @author swiftech 2019-08-22
 **/
@ApiModel
public class GoodsCreateCommand extends BasePopulateCommand<GoodsEntity> {

    @ApiModelProperty(value = "商品名称", example = "闲趣清闲薄脆饼干")
    @JsonProperty("name")
    @Length(max = 16)
    private String name;

    @ApiModelProperty(value = "商品描述", example = "清闲不腻，松脆松化")
    @JsonProperty("description")
    @Length(max = 64)
    private String description;

    @ApiModelProperty(value = "商品价格", example = "12.5")
    @JsonProperty("price")
    @NotNull
    private Double price;

    @ApiModelProperty(value = "生产时间", example = "2020-01-16 00:00:00")
    @JsonProperty("production_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime productionTime;

    @ApiModelProperty(value = "过期日期", example = "2021-01-16")
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

    public LocalDateTime getProductionTime() {
        return productionTime;
    }

    public GoodsCreateCommand setProductionTime(LocalDateTime productionTime) {
        this.productionTime = productionTime;
        return this;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public GoodsCreateCommand setExpireDate(LocalDate expireDate) {
        this.expireDate = expireDate;
        return this;
    }
}
