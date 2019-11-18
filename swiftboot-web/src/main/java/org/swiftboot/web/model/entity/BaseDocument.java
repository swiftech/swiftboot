package org.swiftboot.web.model.entity;

import io.swagger.annotations.ApiModel;
import org.swiftboot.web.annotation.PropertyDescription;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Base class for MongoDB document
 *
 * @author swiftech
 **/
@ApiModel
public abstract class BaseDocument implements Persistent {

    /**
     * 唯一标识
     */
    @PropertyDescription(value = "Entity ID", example = "basedocu20191119010450543ekxpvom")
    @Id
    private String id;

    /**
     * 创建时间
     */
    @PropertyDescription(value = "Creation time", example = "1545355038524")
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 修改时间
     */
    @PropertyDescription(value = "Updating time", example = "1545355038524")
    @Column(name = "update_time")
    private Long updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Long getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public Long getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
