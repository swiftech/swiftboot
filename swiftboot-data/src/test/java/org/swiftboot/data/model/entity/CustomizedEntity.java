package org.swiftboot.data.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "UT_CUSTOMIZED_TABLE")
public class CustomizedEntity implements IdPersistable {

    @Id()
    @Column(name = "ID", length = 32)
    private String id;

    @Column
    private String name;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
