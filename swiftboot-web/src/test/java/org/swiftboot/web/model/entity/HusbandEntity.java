package org.swiftboot.web.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen 2019-04-17
 **/
@Entity
@Table(name = "HUSBAND_TABLE")
public class HusbandEntity extends BaseEntity {
    @Column
    private
    String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "WIFE_ID")
    private
    WifeEntity wife;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WifeEntity getWife() {
        return wife;
    }

    public void setWife(WifeEntity wife) {
        this.wife = wife;
    }
}
