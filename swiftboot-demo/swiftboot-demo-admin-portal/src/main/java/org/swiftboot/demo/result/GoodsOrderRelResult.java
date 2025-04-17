package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.GoodsOrderRelEntity;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 商品订单关系
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsOrderRelResult extends BasePopulateResult<GoodsOrderRelEntity> {

    @Schema(description = "商品ID", example = "e8af5ea376fde35fb2c504633f55b128")
    @JsonProperty("goods_id")
    @PopulateIgnore
    private String goodsId;

    @Schema(description = "订单ID", example = "527d36e654f9eaea6a9b46380d253fc9")
    @JsonProperty("order_id")
    private String orderId;

    @Schema(description = "修改时间", example = "1545355038524")
    @JsonProperty("update_time")
    private Long updateTime;

    @Schema(description = "创建时间", example = "1545355038524")
    @JsonProperty("create_time")
    private Long createTime;

    @Schema(description = "是否逻辑删除", example = "false")
    @JsonProperty("is_delete")
    private Boolean isDelete;

    @Schema(description = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @JsonProperty("id")
    private String id;

    /**
     * 获取商品ID
     *
     * @return
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品ID
     *
     * @param goodsId
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
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
