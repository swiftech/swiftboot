package org.swiftboot.web.model.entity;

import javax.persistence.*;

/**
 * @author Allen 2019-04-17
 **/
@Entity
@Table(name = "WIFE_TABLE")
public class WifeEntity extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "HUSBAND_ID")
    private HusbandEntity parent;

    @Column
    private String name;

    public HusbandEntity getParent() {
        return parent;
    }

    public void setParent(HusbandEntity parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
