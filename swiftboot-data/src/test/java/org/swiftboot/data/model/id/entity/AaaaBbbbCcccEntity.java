package org.swiftboot.data.model.id.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.swiftboot.data.model.entity.BaseIdEntity;

/**
 * @author swiftech 2019-04-08
 **/
@Description("测试实体类")
@Entity
@Table(name = "TEST_TABLE")
public class AaaaBbbbCcccEntity extends BaseIdEntity {
    /**
     * 名称
     */
    @PropertyDescription(value = "名称", example = "闲趣清闲薄脆饼干")
    @Column(name = "NAME", length = 16)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
