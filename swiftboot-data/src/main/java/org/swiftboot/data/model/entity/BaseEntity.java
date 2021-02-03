package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author swiftech
 **/
@MappedSuperclass
public abstract class BaseEntity extends BaseIdEntity implements Persistent {

    /**
     * 创建时间
     */
    @PropertyDescription(value = "Creation time", example = "1545355038524")
    @Column(name = "CREATE_TIME", columnDefinition = "BIGINT COMMENT 'Creation time'")
    private Long createTime;

    /**
     * 修改时间
     */
    @PropertyDescription(value = "Updating time", example = "1545355038524")
    @Column(name = "UPDATE_TIME", columnDefinition = "BIGINT COMMENT 'Updating time'")
    private Long updateTime;

    /**
     * 是否逻辑删除
     */
    @PropertyDescription(value = "Is deleted", example = "false")
    @Column(name = "IS_DELETE", columnDefinition = "BIT DEFAULT FALSE COMMENT 'Is deleted'")
    private Boolean isDelete = Boolean.FALSE;

    public BaseEntity() {
        this.createTime = System.currentTimeMillis();
    }

    public BaseEntity(String id) {
        super(id);
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
