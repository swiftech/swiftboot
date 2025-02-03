package org.swiftboot.data.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "UT_ORPHAN_TABLE")
public class OrphanEntity extends BaseEntity {

    @Column
    private
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
