package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 *
 * @author swiftech
 * @deprecated
 **/
@MappedSuperclass
public abstract class BaseEntity extends BaseLongTimeEntity implements LogicalDeletePersistable<Boolean> {

    /**
     * 是否逻辑删除
     */
    @PropertyDescription(value = "Is deleted", example = "false")
    @Column(name = "IS_DELETE")
    private Boolean isDelete = Boolean.FALSE;

    public BaseEntity() {
    }

    public BaseEntity(String id) {
        super(id);
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    @Override
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

}
