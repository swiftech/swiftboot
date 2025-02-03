package org.swiftboot.data.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * This is used for testing BaseBoolDeleteEntity
 *
 * @author swiftech
 */
@Entity
@Table(name = "UT_BOOL_LOGICAL_DELETE")
public class BoolLogicalDeleteEntity extends BaseBoolDeleteEntity {

    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public BoolLogicalDeleteEntity() {
    }

    public BoolLogicalDeleteEntity(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
