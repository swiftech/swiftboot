package org.swiftboot.data.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "CHILD_TABLE")
public class ChildEntity extends BaseLongTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private ParentEntity parent;

    @Column
    private String name;

    public ParentEntity getParent() {
        return parent;
    }

    public void setParent(ParentEntity parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
