package org.swiftboot.data.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "UT_TOY_TABLE")
public class ToyEntity extends BaseLongTimeEntity {

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.EAGER)
    private ChildEntity child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChildEntity getChild() {
        return child;
    }

    public void setChild(ChildEntity child) {
        this.child = child;
    }
}
