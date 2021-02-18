package org.swiftboot.data.model.entity;

import java.io.Serializable;

/**
 * 具有唯一标识符（ID）的可持久化对象
 *
 * @author swiftech
 **/
public interface IdPersistable extends Serializable {

    /**
     * 获取模型对象的唯一标识，一般为32位的字符串
     *
     * @return
     */
    String getId();

    /**
     * 设置模型对象的唯一标识，一般为32位的字符串
     *
     * @return
     */
    void setId(String id);
}
