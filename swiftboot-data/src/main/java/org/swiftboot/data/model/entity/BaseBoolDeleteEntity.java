package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * Base entity class with bool type delete flag column.
 *
 * @author swiftech
 * @since 2.1.0
 */
@MappedSuperclass
public abstract class BaseBoolDeleteEntity extends BaseIdEntity implements LogicalDeletePersistable<Boolean> {

    /**
     * 是否逻辑删除
     */
    @PropertyDescription(value = "Is logically deleted", example = "false")
    @Column(name = "IS_DELETE")
    private Boolean isDelete = Boolean.FALSE;

    public BaseBoolDeleteEntity() {
    }

    public BaseBoolDeleteEntity(String id) {
        super(id);
    }

    public BaseBoolDeleteEntity(String id, Boolean isDelete) {
        super(id);
        this.isDelete = isDelete;
    }

    @Override
    public Boolean getIsDelete() {
        return isDelete;
    }

    @Override
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
