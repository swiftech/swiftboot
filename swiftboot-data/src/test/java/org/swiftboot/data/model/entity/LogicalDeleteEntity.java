package org.swiftboot.data.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * This is used for testing BaseBoolDeleteEntity
 *
 * @author swiftech
 */
@Entity
public class LogicalDeleteEntity extends BaseBoolDeleteEntity {

    @Column(name = "DESCRIPTION", columnDefinition = "BIT DEFAULT FALSE COMMENT 'This is used for testing BaseBoolDeleteEntity'")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
