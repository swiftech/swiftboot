package org.swiftboot.data.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "UT_PARENT_DATE_TIME_TABLE")
public class ParentDateTimeEntity extends BaseDateTimeEntity {

    @Column
    private
    String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private
    List<ChildDateTimeEntity> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildDateTimeEntity> getItems() {
        return items;
    }

    public void setItems(List<ChildDateTimeEntity> items) {
        this.items = items;
    }

}
