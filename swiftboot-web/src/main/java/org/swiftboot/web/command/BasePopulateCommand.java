package org.swiftboot.web.command;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.apache.commons.beanutils.PropertyUtils;
import org.swiftboot.collections.CollectionUtils;
import org.swiftboot.data.model.entity.Persistent;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.GenericUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.annotation.PopulateIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 提供将参数填入实体类的方法 populateEntity() 和创建 Command 类所对应的实体类的方法 createEntity()
 * 要求实体类 E 必须有无参数的构造函数，除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
 * Command 中存在的属性实体类也必须存在（名称和类型一一对应），否则抛出异常。
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
        Class<P> entityClass = (Class<P>) GenericUtils.ancestorGenericClass(getClass());

        if (entityClass == null) {
            throw new RuntimeException(Info.get(BasePopulateCommand.class, R.REFLECT_TYPE_OF_ENTITY_FAIL));
        }

        try {
            Constructor<P> constructor = entityClass.getConstructor();
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateCommand.class, R.CONSTRUCT_ENTITY_FAIL1, entityClass));
        }
        this.doPopulate(entityClass, ret, true);
        return ret;
    }

    /**
     * 将 Command 中的属性值填充至实体类中，包括继承自 BasePopulateCommand 的类实例和集合。
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Command 中存在的属性实体类也必须存在，否则抛出异常。
     *
     * @param entity
     * @return
     */
    public P populateEntity(P entity) {
        this.doPopulate((Class<P>) entity.getClass(), entity, true);
        return entity;
    }

    /**
     * 将 Command 中的属性值填充至实体类中。
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Command 中存在的属性实体类也必须存在，否则抛出异常。
     *
     * @param entity
     * @return
     * @since 1.1
     */
    public P populateEntityNoRecursive(P entity) {
        this.doPopulate((Class<P>) entity.getClass(), entity, false);
        return entity;
    }

    /**
     * internal populating
     *
     * @param entityClass
     * @param entity
     * @param recursive
     */
    private void doPopulate(Class<P> entityClass, P entity, boolean recursive) {
        Collection<Field> allFields = BeanUtils.getFieldsIgnore(this.getClass(), JsonIgnore.class, PopulateIgnore.class);
        for (Field srcField : allFields) {
            // 处理嵌套
            if (recursive) {
                if (BasePopulateCommand.class.isAssignableFrom(srcField.getType())) {
                    BasePopulateCommand sub = (BasePopulateCommand) BeanUtils.forceGetProperty(this, srcField);
                    if (sub == null) {
                        continue;
                    }
                    try {
                        Persistent relEntity = sub.createEntity();
                        BeanUtils.forceSetProperty(entity, srcField.getName(), relEntity);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    continue;
                }
                else if (Collection.class.isAssignableFrom(srcField.getType())) {
                    Collection items = (Collection) BeanUtils.forceGetProperty(this, srcField);
                    try {
                        Field targetField = BeanUtils.getDeclaredField(entity, srcField.getName());
                        Collection c = CollectionUtils.constructCollectionByType(targetField.getType());
                        for (Object item : items) {
                            if (item instanceof BasePopulateCommand) {
                                Persistent childEntity = ((BasePopulateCommand) item).createEntity();
                                c.add(childEntity);
                            }
                        }
                        BeanUtils.forceSetProperty(entity, srcField.getName(), c);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    continue;
                }
            }
            else {
                if (BasePopulateCommand.class.isAssignableFrom(srcField.getType())
                        || Collection.class.isAssignableFrom(srcField.getType())) {
                    continue;
                }
            }

            // 处理当前对象的值域
            Field targetField = null;
            try {
                targetField = BeanUtils.getDeclaredField(entityClass, srcField.getName());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(Info.get(R.class, R.FIELD_REQUIRED_FOR_ENTITY2, entityClass, srcField.getName()));
            }
            if (targetField == null) {
                throw new RuntimeException(Info.get(R.class, R.FIELD_REQUIRED_FOR_ENTITY2, entityClass, srcField.getName()));
            }
            boolean accessible = targetField.isAccessible();
            targetField.setAccessible(true);
            try {
                Object value = PropertyUtils.getProperty(this, srcField.getName());
                targetField.set(entity, value);
            } catch (Exception e) {
                throw new RuntimeException(Info.get(R.class, R.POPULATE_FIELD_FAIL1, srcField.getName()));
            }
            targetField.setAccessible(accessible);
        }
    }

}
