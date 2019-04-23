package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.model.entity.Persistent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;

/**
 * 提供将参数填入实体类的方法 populateEntity() 和创建 Command 类所对应的实体类的方法 createEntity()
 * 要求实体类 E 必须有无参数的构造函数。
 *
 * @param <P> 对应的实体类
 * @author swiftech
 */
@ApiModel
public abstract class BasePopulateCommand<P extends Persistent> extends HttpCommand {

    /**
     * 创建对应的实体类 P 的实例并且用属性值填充实例
     *
     * @return
     */
    public P createEntity() {
        P ret;
        Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass == null) {
            throw new RuntimeException("反射错误");
        }
        if (!(genericSuperclass instanceof ParameterizedType)) {
            // 如果存在集成，则向上取一级
            genericSuperclass = ((Class)genericSuperclass).getGenericSuperclass();
            if (genericSuperclass == null) {
                throw new RuntimeException("反射错误");
            }
            if (!(genericSuperclass instanceof ParameterizedType)) {
                throw new RuntimeException(
                        String.format("父类%s及其父类都没有指定继承自Persistent的泛型对象，无法创建实体类", getClass().getGenericSuperclass().getTypeName()));
            }
        }

        Class<P> entityClass = (Class<P>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
        if (entityClass == null) {
            throw new RuntimeException("反射错误，无法获取实体类的类型(Class)");
        }

        try {
            Constructor<P> constructor = entityClass.getConstructor();
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("实例化实体类失败: " + entityClass);
        }
        this.doPopulate(entityClass, ret);
        return ret;
    }

    /**
     * 将 Command 中的属性值填充至实体类中。
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Command 中存在的属性实体类也必须存在，否则抛出异常。
     *
     * @param entity
     * @return
     */
    public P populateEntity(P entity) {
        this.doPopulate((Class<P>) entity.getClass(), entity);
        return entity;
    }

    /**
     * internal share
     *
     * @param entityClass
     * @param entity
     */
    private void doPopulate(Class<P> entityClass, P entity) {
        Collection<Field> allFields = BeanUtils.getFieldsIgnore(this.getClass(), JsonIgnore.class, PopulateIgnore.class);
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
                targetField.set(entity, value);
            } catch (Exception e) {
                throw new RuntimeException(String.format("复制属性失败 %s", srcField.getName()));
            }
            targetField.setAccessible(accessible);
        }
    }

}
