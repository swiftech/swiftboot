package org.swiftboot.data.id.entity;

import org.springframework.context.annotation.Description;
import org.swiftboot.data.annotation.PropertyDescription;
import org.swiftboot.data.model.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author swiftech 2019-04-08
 **/
@Description("测试实体类")
@Entity
@Table(name = "TEST_TABLE")
public class AEntity extends BaseEntity {
    /**
     * 名称
     */
    @PropertyDescription(value = "名称", example = "闲趣清闲薄脆饼干")
    @Column(name = "NAME", length = 16, columnDefinition = "VARCHAR(16) COMMENT '名称'")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
