package org.swiftboot.web.dto;

import java.util.List;

/**
 * 列表查询结果抽象类
 *
 * @param <T> 集合中元素的类型
 * @author swiftech
 */
public abstract class BaseListableDto<T extends Dto> implements Dto {

    public abstract List<T> getItems();

    public abstract void setItems(List<T> items);

}
