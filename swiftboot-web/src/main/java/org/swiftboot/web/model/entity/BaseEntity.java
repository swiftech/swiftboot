package org.swiftboot.web.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author swiftech
 **/
@MappedSuperclass
public abstract class BaseEntity extends BaseIdEntity implements Persistent {

    @ApiModelProperty(value = "创建时间", example = "1545355038524")
    @Column(name = "CREATE_TIME", columnDefinition = "BIGINT COMMENT '创建时间'")
    private Long createTime;

    @ApiModelProperty(value = "修改时间", example = "1545355038524")
    @Column(name = "UPDATE_TIME", columnDefinition = "BIGINT COMMENT '修改时间'")
    private Long updateTime;

    @ApiModelProperty(value = "是否逻辑删除", example = "false")
    @Column(name = "IS_DELETE", columnDefinition = "BIT DEFAULT FALSE COMMENT '是否逻辑删除'")
    private Boolean isDelete = Boolean.FALSE;

    public BaseEntity() {
        this.createTime = System.currentTimeMillis();
    }

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean isDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}
