package org.swiftboot.web.result;

import java.util.List;

/**
 * 列表查询结果抽象类
 *
 * @author swiftech
 * @param <T> 集合中元素的类型
 */
public abstract class BaseListableResult<T extends Result> implements Result{

    public abstract List<T> getItems();

    public abstract void setItems(List<T> items);

}
