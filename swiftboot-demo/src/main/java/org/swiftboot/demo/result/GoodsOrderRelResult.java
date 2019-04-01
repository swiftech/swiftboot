package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.web.result.BasePopulateResult;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品订单关系
 *
 * @author swiftech 2019-01-15
 **/
@ApiModel
public class GoodsOrderRelResult extends BasePopulateResult {

    @ApiModelProperty(value = "修改时间", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @ApiModelProperty(value = "商品ID", example = "7f46aa44e0724ef9af0319a0769bd091")
    @JsonProperty("demo_goods_id")
    private String demoGoodsId;

    @ApiModelProperty(value = "创建时间", example = "1545355038524")
    @JsonProperty("create_time")
    private Long createTime;

    @ApiModelProperty(value = "是否逻辑删除", example = "false")
    @JsonProperty("is_delete")
    private Boolean isDelete;

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @JsonProperty("id")
    private String id;

    @ApiModelProperty(value = "订单ID", example = "ea8e6cc03954c56948680b38d9768666")
    @JsonProperty("demo_order_id")
    private String demoOrderId;


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
     * 获取商品ID
     *
     * @return
     */
    public String getDemoGoodsId() {
        return demoGoodsId;
    }

    /**
     * 设置商品ID
     *
     * @param demoGoodsId
     */
    public void setDemoGoodsId(String demoGoodsId) {
        this.demoGoodsId = demoGoodsId;
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

    /**
     * 获取订单ID
     *
     * @return
     */
    public String getDemoOrderId() {
        return demoOrderId;
    }

    /**
     * 设置订单ID
     *
     * @param demoOrderId
     */
    public void setDemoOrderId(String demoOrderId) {
        this.demoOrderId = demoOrderId;
    }


}
