package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.swiftboot.demo.model.entity.GoodsEntity;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 商品
 *
 * @author swiftech 2019-04-07
 **/
@ApiModel
public class GoodsResult extends BasePopulateResult<GoodsEntity> {

    @ApiModelProperty(value = "商品名称", example = "闲趣清闲薄脆饼干")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(value = "商品描述", example = "清闲不腻，松脆松化")
    @JsonProperty("description")
    private String description;

    @ApiModelProperty(value = "商品价格", example = "12.5")
    @JsonProperty("price")
    private Double price;

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

    @ApiModelProperty(value = "商品详情")
    @JsonProperty("goods_detail")
    private GoodsDetailResult goodsDetail;

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

    public GoodsDetailResult getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(GoodsDetailResult goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
