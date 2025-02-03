package org.swiftboot.data.model.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "UT_PARENT_TABLE")
public class ParentEntity extends BaseLongTimeEntity implements Serializable {
    @Column
    private
    String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "parent", orphanRemoval = true)
    private
    List<ChildEntity> items = new ArrayList<>();

    public ParentEntity() {
    }

    public ParentEntity(String name) {
        this.name = name;
    }

    public ParentEntity(String id, String name) {
        super(id);
        this.name = name;
    }

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

    @Override
    public String toString() {
        return "ParentEntity{" +
                "id='" + getId() + "', " +
                "name='" + name + '\'' +
                '}';
    }
}
