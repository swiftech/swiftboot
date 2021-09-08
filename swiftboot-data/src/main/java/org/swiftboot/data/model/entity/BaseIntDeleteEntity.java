package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base entity class with int type delete flag column.
 * value 0 is not deleted, value 1 is deleted.
 *
 * @author swiftech
 * @since 2.1.0
 */
@MappedSuperclass
public abstract class BaseIntDeleteEntity extends BaseIdEntity implements LogicalDeletePersistable<Integer> {

    /**
     * 是否逻辑删除
     */
    @PropertyDescription(value = "Is logically deleted", example = "false")
    @Column(name = "IS_DELETE", columnDefinition = "TINYINT DEFAULT 0 COMMENT 'Is logically deleted'")
    private Integer isDelete = 0;

    @Override
    public Integer getIsDelete() {
        return isDelete;
    }

    @Override
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}
