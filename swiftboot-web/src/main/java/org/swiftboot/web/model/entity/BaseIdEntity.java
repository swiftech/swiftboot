package org.swiftboot.web.model.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * 带有 ID 字段的实体类
 *
 * @author swiftech
 **/
@MappedSuperclass
public abstract class BaseIdEntity implements IdPojo {

    @ApiModelProperty(value = "唯一标识", example = "441a3c4cbe574f17b2a3dc3fb5cda1c4")
    @Id()
    @Column(name = "ID", columnDefinition = "char(32) COMMENT '唯一标识'")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
