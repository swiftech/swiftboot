package org.swiftboot.web.result;

import org.swiftboot.data.model.entity.IdPersistable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表查询结果抽象类
 *
 * @param <T> 集合中元素的类型
 * @param <E> 元素对应的实体类类型
 * @author swiftech
 */
public abstract class BasePopulateListResult<T extends BasePopulateResult<E>, E extends IdPersistable> extends BaseListableResult<T> {

    /**
     * 从实体类集合创建相对应的返回对象集合
     *
     * @param entities
     * @return
     */
    public BasePopulateListResult<T, E> populateByEntities(Iterable<E> entities) {
        // TODO call populateByEntities(Iterable<E> entities, PopulateHandler<T, E> populateHandler) instead
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

    /**
     * 从实体类集合创建相对应的返回对象集合
     *
     * @param entities
     * @param populateHandler Handle after each element populated
     * @return
     * @since 1.1
     */
    public BasePopulateListResult<T, E> populateByEntities(Iterable<E> entities, PopulateHandler<T, E> populateHandler) {
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
            if (populateHandler != null) {
                populateHandler.onPopulated(item, entity);
            }
            list.add(item);
        }
        this.setItems(list);
        return this;
    }

    /**
     * 处理集合填充的回调
     *
     * @param <T>
     * @param <E>
     */
    public interface PopulateHandler<T extends BasePopulateResult<E>, E extends IdPersistable> {

        /**
         * 一个继承自 BasePopulateResult 的类被填充完成之后执行
         *
         * @param result
         * @param entity
         */
        void onPopulated(T result, E entity);
    }
}
