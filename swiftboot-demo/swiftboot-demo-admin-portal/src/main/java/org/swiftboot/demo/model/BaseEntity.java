package org.swiftboot.demo.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.swiftboot.data.model.entity.BaseBoolDeleteEntity;

/**
 * @since 3.1
 */
@MappedSuperclass
@AttributeOverrides({
        @AttributeOverride(
                name = "id", // 父类中ID字段的名称
                column = @Column(name = "ID", length = 32, nullable = false) // 子类指定的长度和约束
        )
})
public class BaseEntity extends BaseBoolDeleteEntity {

    public BaseEntity() {
    }

    public BaseEntity(String id) {
        super(id);
    }
}
