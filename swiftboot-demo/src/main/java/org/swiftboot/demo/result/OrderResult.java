package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.demo.model.entity.OrderEntity;
import org.swiftboot.web.result.BasePopulateResult;

import java.util.Set;

/**
 * 订单
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderResult extends BasePopulateResult<OrderEntity> {

    @ApiModelProperty(value = "订单编号", example = "2019032411081201")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单描述", example = "越快越好")
    @JsonProperty("description")
    private String description;

    @ApiModelProperty(value = "商品总数", example = "5")
    @JsonProperty("total_count")
    private Integer totalCount;

    @ApiModelProperty(value = "发货地址", example = "极乐世界102号")
    @JsonProperty("address")
    private String address;

    @ApiModelProperty(value = "修改时间", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @ApiModelProperty(value = "创建时间", example = "1545355038524")
    @JsonProperty("create_time")
    private Long createTime;

    @ApiModelProperty(value = "是否逻辑删除", example = "false")
    @JsonProperty("is_delete")
    private Boolean isDelete;

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @JsonProperty("id")
    private String id;

    private Set<OrderDetailResult> orderDetails;

    /**
     * 获取订单编号
     *
     * @return
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置订单编号
     *
     * @param orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 获取订单描述
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置订单描述
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取商品总数
     *
     * @return
     */
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * 设置商品总数
     *
     * @param totalCount
     */
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 获取发货地址
     *
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置发货地址
     *
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取修改时间
     *
     * @return
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建时间
     *
     * @return
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取是否逻辑删除
     *
     * @return
     */
    public Boolean getIsDelete() {
        return isDelete;
    }

    /**
     * 设置是否逻辑删除
     *
     * @param isDelete
     */
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取唯一标识
     *
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * 设置唯一标识
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    public Set<OrderDetailResult> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetailResult> orderDetails) {
        this.orderDetails = orderDetails;
    }
}