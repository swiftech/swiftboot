package org.swiftboot.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.swiftboot.data.model.entity.BaseInstantEntity;

@Entity
@Table(name = "timezone_based")
public class TimezoneBasedEntity extends BaseInstantEntity {

    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
