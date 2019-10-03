package org.swiftboot.web.result;

import org.swiftboot.web.model.entity.Persistent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表查询结果抽象类
 *
 * @author swiftech
 * @param <T> 集合中元素的类型
 * @param <E> 元素对应的实体类类型
 */
public abstract class BasePopulateListResult<T extends BasePopulateResult, E extends Persistent> extends BaseListableResult<T> {

    /**
     * 从实体类集合创建相对应的返回对象集合
     *
     * @param entities
     * @return
     */
    public BasePopulateListResult<T, E> populateByEntities(Iterable<E> entities) {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException("反射错误");
        }
        Class<T> itemClass = (Class<T>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if (itemClass == null) {
            throw new RuntimeException("类定义缺少元素的类型");
        }

        List<T> list = new ArrayList<>();
        for (E entity : entities) {
            T item = BasePopulateResult.createResult(itemClass, entity);
            list.add(item);
        }
        this.setItems(list);
        return this;
    }

}
