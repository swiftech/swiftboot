package org.swiftboot.web.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.collections.CollectionUtils;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.util.GenericUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.annotation.PopulateIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * 提供将参数填入实体对象的方法 populateEntity() 和创建 Request 类所对应的实体类实例的方法 createEntity()
 * 要求实体类 E 必须有无参数的构造函数，除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
 * Request 中存在的属性实体类也必须存在（名称和类型一一对应），否则抛出异常。
 *
 * @param <P> 对应的实体类
 * @author swiftech
 */
public abstract class BasePopulateRequest<P extends IdPersistable> extends HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(BasePopulateRequest.class);

    /**
     * 创建对应的实体类 P 的实例并且用属性值填充实例，
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Request 中存在的属性实体类也必须存在（名称和类型一一对应），否则抛出异常。
     *
     * @return
     */
    public P createEntity() {
        P ret;
        Class<P> entityClass = (Class<P>) GenericUtils.ancestorGenericClass(getClass());

        if (entityClass == null) {
            throw new RuntimeException(Info.get(BasePopulateRequest.class, R.REFLECT_TYPE_OF_ENTITY_FAIL));
        }

        try {
            Constructor<P> constructor = entityClass.getConstructor();
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateRequest.class, R.CONSTRUCT_ENTITY_FAIL1, entityClass));
        }
        this.doPopulate(entityClass, ret, true);
        return ret;
    }

    /**
     * 将 Request 中的属性值填充至实体类中，包括继承自 BasePopulateRequest 的类实例和集合。
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Request 中存在的属性实体类也必须存在，否则抛出异常。
     *
     * @param entity
     * @return
     */
    public P populateEntity(P entity) {
        this.doPopulate((Class<P>) entity.getClass(), entity, true);
        return entity;
    }

    /**
     * 将 Request 中的属性值填充至实体类中。
     * 除了用注解 {@link JsonIgnore} 或 {@link PopulateIgnore} 标注的属性之外，
     * Request 中存在的属性实体类也必须存在，否则抛出异常。
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
                Field targetField;
                Object target;
                try {
                    targetField = BeanUtils.getDeclaredField(entity, srcField.getName());
                    target = BeanUtils.forceGetProperty(entity, targetField);
                } catch (Exception e) {
                    log.warn(e.getLocalizedMessage());
                    continue;
                }
                if (BasePopulateRequest.class.isAssignableFrom(srcField.getType())) {
                    BasePopulateRequest<IdPersistable> sub = (BasePopulateRequest<IdPersistable>) BeanUtils.forceGetProperty(this, srcField);
                    if (sub == null) {
                        // System.out.printf("Ignore populate field: %s%n", srcField);
                        continue;
                    }
                    try {
                        if (target == null) {
                            // System.out.println("Create sub entity");
                            IdPersistable relEntity = sub.createEntity();
                            BeanUtils.forceSetProperty(entity, targetField, relEntity);
                        }
                        else {
                            // System.out.println("Populate sub entity");
                            IdPersistable relEntity = (IdPersistable) target;
                            sub.populateEntity(relEntity);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    continue;
                }
                else if (Collection.class.isAssignableFrom(srcField.getType())) {
                    Collection<?> items = (Collection<?>) BeanUtils.forceGetProperty(this, srcField);
                    try {
                        if (target == null) {
                            // Populate collections for new created entity.
                            Collection<Object> newEntities = CollectionUtils.constructCollectionByType((Class<Collection<Object>>) targetField.getType());
                            items.forEach(item -> {
                                if (!(item instanceof BasePopulateRequest)) return;// exclude non populatable elements;
                                newEntities.add(((BasePopulateRequest) item).createEntity());
                            });
                            BeanUtils.forceSetProperty(entity, targetField, newEntities);
                        }
                        else {
                            if (target instanceof Collection) { // populate collections for existed entities.
                                Collection subEntities = (Collection) target;
                                items.forEach(item -> {
                                    if (!(item instanceof BasePopulateRequest))
                                        return; // exclude non populate-able elements;
                                    BasePopulateRequest<?> populateableItem = (BasePopulateRequest<?>) item;
                                    IdPersistable subEntity = populateableItem.createEntity();
                                    if (subEntities.contains(subEntity)) { // populate to existed entity to merge (for updating sub entities)
                                        Optional<IdPersistable> optMatched = subEntities.stream().filter(
                                                (Predicate<IdPersistable>) o -> subEntity.getId().equals(o.getId())
                                        )
                                                .findFirst();
                                        if (optMatched.isPresent()) {
                                            IdPersistable oldEntity = optMatched.get();
                                            ((BasePopulateRequest) item).populateEntity(oldEntity);
                                        }
                                    }
                                    else { // New sub entity
                                        subEntities.add(subEntity);
                                    }
                                });
                            }
                            else {// src type is collection but target type isn't.
                                continue;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                    continue;
                }
            }
            else { // no recursive
                if (BasePopulateRequest.class.isAssignableFrom(srcField.getType())
                        || Collection.class.isAssignableFrom(srcField.getType())) {
                    continue; // Ignore nested requests if no recursive.
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
            try {
                Object value = BeanUtils.forceGetProperty(this, srcField);
                BeanUtils.forceSetProperty(entity, targetField, value);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(Info.get(R.class, R.POPULATE_FIELD_FAIL1, srcField.getName()));
            }
        }
    }

}
