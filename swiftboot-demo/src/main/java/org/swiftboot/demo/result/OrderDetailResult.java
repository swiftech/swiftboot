package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 订单明细
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class OrderDetailResult extends BasePopulateResult {

    @ApiModelProperty(value = "订单ID", example = "f80dc17d1b744a38a438e47c8d95cbd1")
    @JsonProperty("order_id")
    @PopulateIgnore
    private String orderId;

    @ApiModelProperty(value = "明细描述")
    @JsonProperty("description")
    private String description;

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

}
