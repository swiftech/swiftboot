package org.swiftboot.web.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.annotation.PopulateIgnore;
import org.swiftboot.web.model.entity.Persistent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 提供将入实体类的属性值填入返回值的方法。如果 Result 存在的属性名称在实体类中不存在的话，则抛出异常。
 * <code>populateByEntity(E entity)</code> 方法将实体类的属性填充到当前对象实例中。
 * 静态方法<code>populateByEntity(E entity, BasePopulateResult<E> result)</code>将指定实体类属性值填充至指定的 Result 实例中。
 * 标记 {@link JsonIgnore} 注解的 Result 类属性不会被处理。
 * 如果希望某个实体类中不存在的属性也能出现在 Result 类中，那么可以用 {@link PopulateIgnore} 来标注这个属性。
 *
 * @author swiftech
 **/
public abstract class BasePopulateResult<E extends Persistent> implements Result{

//    @JsonIgnore
//    @PopulateIgnore
//    private Logger log = LoggerFactory.getLogger(BasePopulateResult.class);

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
            throw new RuntimeException("Result类缺少无参数构造函数");
        }
        try {
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Result类缺少无参数构造函数");
        }
        ret.populateByEntity(entity);
        return ret;
    }

    /**
     * 从实体类填充当前返回值对象，如果属性值是其他对象的集合，那么也会自动从实体类中获取对应名字的集合来填充返回值的集合
     * @param entity
     * @return
     */
    public BasePopulateResult<E> populateByEntity(E entity) {
        return populateByEntity(entity, this);
    }

    /**
     * 从实体类填充属性至返回值对象，如果属性值是其他对象的集合，那么也会自动从实体类中获取对应名字的集合来填充返回值的集合
     *
     * @param entity
     * @return
     */
    public static <E extends Persistent> BasePopulateResult<E> populateByEntity(E entity, BasePopulateResult<E> result) {
        if (entity == null) {
            throw new RuntimeException("实体类为空");
        }
        Logger log = LoggerFactory.getLogger(BasePopulateResult.class);
        log.info("populate result from entity: " + entity);
        Collection<Field> targetFields = BeanUtils.getFieldsIgnore(result.getClass(), JsonIgnore.class, PopulateIgnore.class);
        for (Field targetField : targetFields) {
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
            // 处理集合类属性
            if (Collection.class.isAssignableFrom(srcField.getType())
                    && Collection.class.isAssignableFrom(targetField.getType())) {
                try {
                    Type[] actualTypeArguments = ((ParameterizedType) targetField.getGenericType()).getActualTypeArguments();
                    if (actualTypeArguments.length > 0) {
                        Collection srcCollection = (Collection) BeanUtils.forceGetProperty(entity, srcField.getName());
                        Collection targetCollection = (Collection) BeanUtils.forceGetProperty(result, targetField.getName());
                        if (targetCollection == null) {
                            if (Set.class.isAssignableFrom(targetField.getType())) {
                                targetCollection = new HashSet();
                            }
                            else if (List.class.isAssignableFrom(targetField.getType())) {
                                targetCollection = new ArrayList();
                            }
                            targetField.set(result, targetCollection);
                        }
                        Class clazz = (Class) actualTypeArguments[0];
                        for (Object subEntity : srcCollection) {
                            if (subEntity instanceof Persistent) {
                                BasePopulateResult<E> subResult = createResult(clazz, (Persistent) subEntity);
                                targetCollection.add(subResult);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("填充集合属性失败 %s", srcField.getName()));
                }
            }
            else {
                try {
                    Object value = BeanUtils.forceGetProperty(entity, srcField.getName());
                    targetField.set(result, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(String.format("填充属性失败 %s", srcField.getName()));
                }
            }
            targetField.setAccessible(accessible);
        }
        return result;
    }
}
