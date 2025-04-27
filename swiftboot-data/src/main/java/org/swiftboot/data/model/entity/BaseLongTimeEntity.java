package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * 基于 {@coee java.lang.Long} 的实体类基类。
 *
 * @author swiftech
 * @since 2.0.0
 */
@MappedSuperclass
public abstract class BaseLongTimeEntity extends BaseIdEntity implements TimePersistable<Long> {
    /**
     * 创建时间
     */
    @PropertyDescription(value = "Creation time", example = "1545355038524")
    @Column(name = "CREATE_TIME")
    private Long createTime;
    /**
     * 修改时间
     */
    @PropertyDescription(value = "Updating time", example = "1545355038524")
    @Column(name = "UPDATE_TIME")
    private Long updateTime;

    public BaseLongTimeEntity() {
    }

    public BaseLongTimeEntity(String id) {
        super(id);
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
}
