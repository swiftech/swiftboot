package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.beanutils.PropertyUtils;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.IdUtils;
import org.swiftboot.web.model.entity.Persistent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 提供将参数填入实体类的方法。如果 Command 存在的参数在实体类中不存在的话，则抛出异常。
 * 为了达到目的，要求实体类 E 不能有带参数的构造函数。
 * TODO Nested Properties的处理（忽略？）
 * TODO 用 annotation 实现 converter 或者 formatter
 *
 * @author swiftech
 * @param <E> 对应的实体类
 * @author swiftech 2018-11-21
 */
public abstract class BasePopulateCommand<E extends Persistent> extends HttpCommand {

    public E createEntity() {
        E ret;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException("反射错误");
        }
        System.out.println(genericSuperclass);

        Class<E> entityClass = (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if (entityClass == null) {
            throw new RuntimeException("反射错误");
        }
        System.out.println(entityClass);

        try {
            Constructor<E> constructor = entityClass.getConstructor();
            ret = constructor.newInstance();
            ret.setId(IdUtils.makeUUID());
            ret.setCreateTime(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("创建实体类错误");
        }

        this.doPopulate(entityClass, ret);
        return ret;
    }


    public E populateEntity(E entity) {
        this.doPopulate((Class<E>) entity.getClass(), entity);
        return entity;
    }


    private void doPopulate(Class<E> entityClass, E entityInstance) {
        // 复制所有属性至实体类，如果实体类不存在该属性，则抛出异常
        Collection<Field> allFields = BeanUtils.getFieldsIgnore(this.getClass(), JsonIgnore.class);
        for (Field srcField : allFields) {
            Field targetField = null;
            try {
                targetField = BeanUtils.getDeclaredField(entityClass, srcField.getName());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("实体类 %s 缺少参数必要的属性 %s", entityClass, srcField.getName()));
            }
            if (targetField == null) {
                throw new RuntimeException(String.format("实体类 %s 缺少参数必要的属性 %s", entityClass, srcField.getName()));
            }
            boolean accessible = targetField.isAccessible();
            targetField.setAccessible(true);
            try {
                Object value = PropertyUtils.getProperty(this, srcField.getName());
                targetField.set(entityInstance, value);
            } catch (Exception e) {
                throw new RuntimeException(String.format("复制属性失败 %s", srcField.getName()));
            }
            targetField.setAccessible(accessible);
        }
    }

}
