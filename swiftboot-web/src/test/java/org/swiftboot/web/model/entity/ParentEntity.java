package org.swiftboot.web.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    Set<ChildEntity> items = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ChildEntity> getItems() {
        return items;
    }

    public void setItems(Set<ChildEntity> items) {
        this.items = items;
    }
}
