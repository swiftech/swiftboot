package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 基于 {@coee java.time.LocalDateTime} 的实体类基类。
 *
 * @author swiftech
 * @since 2.0.0
 **/
@MappedSuperclass
public abstract class BaseLocalDateTimeEntity extends BaseIdEntity implements TimePersistable<LocalDateTime> {

    /**
     * 创建时间
     */
    @PropertyDescription(value = "Creation time")
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @PropertyDescription(value = "Updating time")
    @Column(name = "UPDATE_TIME")
    private LocalDateTime updateTime;

    public BaseLocalDateTimeEntity() {
    }

    public BaseLocalDateTimeEntity(String id) {
        super(id);
    }

    @Override
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

}
