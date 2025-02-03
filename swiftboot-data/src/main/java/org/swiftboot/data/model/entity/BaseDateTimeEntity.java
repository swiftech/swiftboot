package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.util.Date;

/**
 * 基于 {@coee java.util.Date} 的实体类基类。
 *
 * @author swiftech
 * @since 2.0.0
 **/
@MappedSuperclass
public abstract class BaseDateTimeEntity extends BaseIdEntity implements TimePersistable<Date> {

    /**
     * 创建时间
     */
    @PropertyDescription(value = "Creation time")
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 修改时间
     */
    @PropertyDescription(value = "Updating time")
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    public BaseDateTimeEntity() {
    }

    public BaseDateTimeEntity(String id) {
        super(id);
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
