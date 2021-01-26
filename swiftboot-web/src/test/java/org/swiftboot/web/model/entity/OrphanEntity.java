package org.swiftboot.web.model.entity;

import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "ORPHAN_TABLE")
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
