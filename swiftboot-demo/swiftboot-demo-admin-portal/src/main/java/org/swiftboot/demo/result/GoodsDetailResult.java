package org.swiftboot.demo.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.swiftboot.demo.model.entity.GoodsDetailEntity;
import org.swiftboot.web.result.BasePopulateResult;

/**
 * 商品详情
 *
 * @author swiftech 2019-04-07
 **/
@Schema
public class GoodsDetailResult extends BasePopulateResult<GoodsDetailEntity> {

    @Schema(description = "商品图片URI", example = "/image/goods/1029")
    @JsonProperty("image_uri")
    private String imageUri;

    @Schema(description = "折扣", example = "0.85")
    @JsonProperty("discount")
    private Double discount;

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
     * 获取商品图片URI
     *
     * @return
     */
    public String getImageUri() {
        return imageUri;
    }

    /**
     * 设置商品图片URI
     *
     * @param imageUri
     */
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * 获取折扣
     *
     * @return
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * 设置折扣
     *
     * @param discount
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
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
