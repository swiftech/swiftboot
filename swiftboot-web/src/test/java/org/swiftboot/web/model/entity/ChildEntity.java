package org.swiftboot.web.model.entity;

import javax.persistence.*;

/**
 * @author Allen 2019-04-17
 **/
@Entity
@Table(name = "CHILD_TABLE")
public class ChildEntity extends BaseEntity {

//    @Column(name = "PARENT_ID")
//    private String parentId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private ParentEntity parentEntity;

    @Column
    private String name;

//    public String getParentId() {
//        return parentId;
//    }
//
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }


    public ParentEntity getParentEntity() {
        return parentEntity;
    }

    public void setParentEntity(ParentEntity parentEntity) {
        this.parentEntity = parentEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
