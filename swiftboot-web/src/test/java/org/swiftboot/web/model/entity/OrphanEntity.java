package org.swiftboot.web.model.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Allen 2019-04-17
 **/
@Entity
@Table(name = "ORPHAN_TABLE")
public class OrphanEntity extends BaseEntity {

    @Column
    private
    String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
