package org.swiftboot.web.dto;

import java.util.List;

/**
 * 列表查询结果抽象类
 *
 * @param <T> 集合中元素的类型
 * @author swiftech
 */
public abstract class BaseListableDto<T extends Dto> implements Dto {

    protected List<T> items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

}
