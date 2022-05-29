package org.swiftboot.data.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * @author swiftech
 **/
@Entity
@Table(name = "UT_CHILD_DATE_TIME_TABLE")
public class ChildDateTimeEntity extends BaseDateTimeEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    @JsonIgnore
    private ParentDateTimeEntity parent;

    @Column
    private String name;

    public ParentDateTimeEntity getParent() {
        return parent;
    }

    public void setParent(ParentDateTimeEntity parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
