package org.swiftboot.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.model.entity.BaseEntity;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.annotation.PopulateIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 提供将入实体对象的属性值填入返回值（DTO）对象的方法。如果 DTO 对象存在的属性名称在实体中对象不存在的话，则抛出异常。
 * <code>populateByEntity(E entity)</code> 方法将实体对象的属性填充到当前对象实例中。
 * 静态方法<code>populateByEntity(E entity, BasePopulateDto<E> dto)</code>将指定实体对象属性值填充至指定的 DTO 实例中。
 * 填充方法支持将实体对象的 <code>@OneToOne</code> 和 </code>@OneToMany</code> 的属性值填充到 DTO 实例中，前提是 DTO 类中对应的属性类型必须也是继承自 BasePopulateDto 的类。
 * 注意：一对一关系如果需要填充，则必须使用 fetch = FetchType.LAZY 来标注，否则将无法通过反射获取到带有属性值的子类。
 * 标记 {@link JsonIgnore} 注解的 DTO 类属性不会被处理。
 * 如果希望某个实体类中不存在的属性也能出现在 DTO 类中，那么可以用 {@link PopulateIgnore} 来标注这个属性。
 *
 * @author swiftech
 **/
public abstract class BasePopulateDto<E extends IdPersistable> implements Dto {

    /**
     * 按照返回值类型创建返回值对象，并从实体对象填充返回值
     *
     * @param dtoClass 返回对象类型
     * @param entity      实体对象
     * @param <T>
     * @return
     */
    public static <T extends BasePopulateDto> T createDto(
            Class<T> dtoClass,
            IdPersistable entity) {
        if (dtoClass == null || entity == null) {
            throw new IllegalArgumentException(Info.get(BasePopulateDto.class, R.REQUIRE_PARAMS));
        }

        T ret;
        Constructor<T> constructor;
        try {
            constructor = dtoClass.getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.REQUIRE_NO_PARAM_CONSTRUCTOR1, dtoClass.getName()));
        }
        try {
            ret = constructor.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.CONSTRUCT_FAIL2, dtoClass.getName(), BasePopulateDto.class));
        }
        ret.populateByEntity(entity);
        return ret;
    }

    /**
     * 从实体对象填充当前返回值对象，如果属性值是其他对象的集合，那么也会自动从实体对象中获取对应名字的集合来填充返回值的集合
     *
     * @param entity
     * @return
     */
    public BasePopulateDto<E> populateByEntity(E entity) {
        return BasePopulateDto.populateByEntity(entity, this);
    }

    /**
     * 从实体对象填充属性至返回值对象，如果属性值是其他对象的集合，那么也会自动从实体对象中获取对应名字的集合来填充返回值的集合
     *
     * @param entity
     * @param dto
     * @return
     */
    public static <E extends IdPersistable> BasePopulateDto<E> populateByEntity(E entity, BasePopulateDto<E> dto) {
        if (entity == null) {
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.REQUIRE_ENTITY));
        }
        Logger log = LoggerFactory.getLogger(BasePopulateDto.class);
        log.trace(Info.get(BasePopulateDto.class, R.POPULATE_FROM_ENTITY1, entity));

        /*
         * 先处理一对多关联（保证ID属性先被处理，后续处理时略过这些字段）
         */
        List<String> ignoredFieldNameList = new LinkedList<>();// 需要忽略的目标属性名称
        List<Field> fieldsByType = BeanUtils.getDeclaredFieldsByType(entity, BaseEntity.class);
        for (Field srcField : fieldsByType) {
            String relationFiledNameInDtoClass = srcField.getName() + "Id";
            try {
                Field targetField = dto.getClass().getDeclaredField(relationFiledNameInDtoClass);
                if (targetField.getAnnotation(JsonIgnore.class) != null
                        || targetField.getAnnotation(PopulateIgnore.class) != null) {
                    continue;
                }

                BaseEntity parentEntity = (BaseEntity) BeanUtils.forceGetProperty(entity, srcField);
                if (parentEntity != null) {
                    BeanUtils.forceSetProperty(dto, relationFiledNameInDtoClass, parentEntity.getId());
                    ignoredFieldNameList.add(relationFiledNameInDtoClass); // 记录目标属性名称
                }
            } catch (Exception e) {
                // 忽略处理
                continue;
            }
        }
        /*
         * 处理（一对一）关联的 DTO 对象
         */
        List<Field> fieldsOne2One = BeanUtils.getDeclaredFieldsByTypeIgnore(dto, BasePopulateDto.class, JsonIgnore.class, PopulateIgnore.class);
        for (Field targetField : fieldsOne2One) {
            ignoredFieldNameList.add(targetField.getName());
            Field srcField;
            try {
                srcField = BeanUtils.getDeclaredField(entity, targetField.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new RuntimeException(Info.get(R.class, R.FIELD_REQUIRED_FOR_ENTITY2, entity.getClass(), targetField.getName()));
            }


//            String getterNameInEntity  = "get" + Character.toUpperCase(srcField.getName().charAt(0)) +  srcField.getName().substring(1);
//            Object subEntity = null;
//            try {
//                subEntity = BeanUtils.forceInvokeMethod(entity, getterNameInEntity);
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//                throw new RuntimeException(String.format("实体对象 %s 缺少参数必要的属性的 Getter: %s", entity.getClass(), targetField.getName()));
//            }

            Object subEntity = BeanUtils.forceGetProperty(entity, srcField);
            if (subEntity instanceof IdPersistable) {
                Class subDtoClass = (Class) targetField.getGenericType();
                if (subDtoClass != null) {
                    BasePopulateDto<E> subDto = createDto(subDtoClass, (IdPersistable) subEntity);
                    BeanUtils.forceSetProperty(dto, targetField, subDto);
                }
            }
        }

        /*
         * 接着处理所有除外键属性之外的所有可用属性
         */
        Collection<Field> targetFields = BeanUtils.getFieldsIgnore(dto.getClass(), JsonIgnore.class, PopulateIgnore.class);
        for (Field targetField : targetFields) {
            if (ignoredFieldNameList.contains(targetField.getName())) {
                continue;
            }
            Field srcField;
            try {
                srcField = BeanUtils.getDeclaredField(entity, targetField.getName());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new RuntimeException(Info.get(R.class, R.FIELD_REQUIRED_FOR_ENTITY2, entity.getClass(), targetField.getName()));
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
                        if (srcCollection != null && !srcCollection.isEmpty()) {
                            Collection targetCollection = (Collection) BeanUtils.forceGetProperty(dto, targetField.getName());
                            Class elementClass = (Class) actualTypeArguments[0];
                            if (targetCollection == null) {
                                if (Set.class.isAssignableFrom(targetField.getType())) {
                                    // 如果集合为 TreeSet 并且其中的元素的类型实现了 Comparable 接口，那么返回 TreeSet
                                    if (TreeSet.class.isAssignableFrom(targetField.getType())
                                            && Comparable.class.isAssignableFrom((Class<?>) elementClass)) {
                                        targetCollection = new TreeSet<>();
                                    }
                                    else {
                                        targetCollection = new HashSet<>();
                                    }
                                }
                                else if (List.class.isAssignableFrom(targetField.getType())) {
                                    targetCollection = new LinkedList<>();
                                }
                                targetField.set(dto, targetCollection);
                            }

                            for (Object subEntity : srcCollection) {
                                if (subEntity instanceof IdPersistable) {
                                    BasePopulateDto<E> subDto = createDto(elementClass, (IdPersistable) subEntity);
                                    targetCollection.add(subDto);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(Info.get(BasePopulateDto.class, R.POPULATE_COLLECTION_FAIL1, srcField.getName()));
                }
            }
            else {
                try {
                    Object value = BeanUtils.forceGetProperty(entity, srcField.getName());
                    targetField.set(dto, value);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(Info.get(R.class, R.POPULATE_FIELD_FAIL1, srcField.getName()));
                }
            }
            targetField.setAccessible(accessible);
        }
        return dto;
    }
}
