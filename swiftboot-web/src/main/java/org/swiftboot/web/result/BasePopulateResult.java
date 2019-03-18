package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.model.entity.Persistent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 提供将入实体类的值填入返回值的方法。如果 Result 存在的参数在实体类中不存在的话，则抛出异常。
 *
 * @author swiftech
 **/
public abstract class BasePopulateResult<E extends Persistent> {

    @JsonIgnore
    @PopulateIgnore
    private Logger log = LoggerFactory.getLogger(BasePopulateResult.class);

    /**
     * 按照返回值类型创建返回值对象，并从实体类填充返回值
     *
     * @param targetClass
     * @param entity
     * @param <T>
     * @return
     */
    public static <T extends BasePopulateResult> T createResult(
            Class<T> targetClass,
            Persistent entity) {
        if (targetClass == null || entity == null) {
            throw new IllegalArgumentException("缺少必要的参数");
        }

        T ret;
        Constructor<T> constructor;
        try {
            constructor = targetClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Result缺少无参数构造函数");
        }
        try {
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Result缺少无参数构造函数");
        }
        ret.populateByEntity(entity);
        return ret;
    }

    public BasePopulateResult<E> populateByEntity(E entity) {
        return populateByEntity(entity, this);
    }

    /**
     * 从实体类填充当前返回值对象
     *
     * @param entity
     * @return
     */
    public BasePopulateResult<E> populateByEntity(E entity, BasePopulateResult result) {
        if (entity == null) {
            throw new RuntimeException("实体类为空");
        }
        log.info("populate result from entity: " + entity);
        Collection<Field> allFields = BeanUtils.getFieldsIgnore(result.getClass(), JsonIgnore.class, PopulateIgnore.class);
        for (Field targetField : allFields) {
            Field srcField;
            try {
                srcField = BeanUtils.getDeclaredField(entity, targetField.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new RuntimeException(String.format("实体类 %s 缺少参数必要的属性 %s", entity.getClass(), targetField.getName()));
            }
            // unlock
            boolean accessible = targetField.isAccessible();
            targetField.setAccessible(true);
            // 处理集合类
            if (Set.class.isAssignableFrom(srcField.getType())
                    || List.class.isAssignableFrom(srcField.getType())) {
                try {
                    targetField.getType().getGenericInterfaces();
                    Collection value = (Collection) BeanUtils.forceGetProperty(entity, srcField.getName());
//                    for (Object subEntity : value) {
//                        populateByEntity(subEntity, );
//                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }
            else {
                try {
                    Object value = BeanUtils.forceGetProperty(entity, srcField.getName());
                    targetField.set(result, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("复制属性失败 %s", srcField.getName()));
                }
            }
            targetField.setAccessible(accessible);
        }
        return result;
    }
}
