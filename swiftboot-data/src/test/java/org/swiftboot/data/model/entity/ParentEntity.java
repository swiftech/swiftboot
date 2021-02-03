package org.swiftboot.data.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "PARENT_TABLE")
public class ParentEntity extends BaseEntity {
    @Column
    private
    String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private
    List<ChildEntity> items = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildEntity> getItems() {
        return items;
    }

    public void setItems(List<ChildEntity> items) {
        this.items = items;
    }
}
