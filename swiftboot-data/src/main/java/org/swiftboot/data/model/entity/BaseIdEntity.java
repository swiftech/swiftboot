package org.swiftboot.data.model.entity;

import org.swiftboot.data.annotation.PropertyDescription;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * 带有 ID 字段的实体类
 *
 * @author swiftech
 **/
@MappedSuperclass
public abstract class BaseIdEntity implements IdPersistable {

    /**
     * 唯一标识
     */
    @PropertyDescription(value = "Entity ID", example = "basident20191119010450544siobnic")
    @Id()
    @Column(name = "ID", columnDefinition = "char(32)")
    private String id;

    public BaseIdEntity() {
    }

    public BaseIdEntity(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (id == null) return false;
        BaseIdEntity that = (BaseIdEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
