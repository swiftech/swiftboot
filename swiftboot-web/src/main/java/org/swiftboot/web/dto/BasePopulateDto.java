package org.swiftboot.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 按照返回值类型创建返回值对象，并从实体对象填充返回值，如果 DTO 中定义了相应的关联对象，那么也会被自动填充。
     *
     * @param dtoClass 返回对象类型
     * @param entity   实体对象
     * @param <T>
     * @return
     */
    public static <T extends BasePopulateDto> T createDto(
            Class<T> dtoClass,
            IdPersistable entity) {
        return createDto(dtoClass, entity, true);
    }

    /**
     * 按照返回值类型创建返回值对象，并从实体对象填充返回值
     *
     * @param dtoClass 返回对象类型
     * @param entity   实体对象
     * @param includeRelation 是否自动填充关联对象
     * @param <T>
     * @return
     */
    public static <T extends BasePopulateDto> T createDto(
            Class<T> dtoClass, IdPersistable entity, boolean includeRelation) {
        if (dtoClass == null || entity == null) {
            throw new IllegalArgumentException(Info.get(BasePopulateDto.class, R.REQUIRE_PARAMS));
        }

        T dto = constructDto(dtoClass);
        dto.populateByEntity(entity, includeRelation);
        return dto;
    }

    private static <T extends BasePopulateDto> T constructDto(Class<T> dtoClass) {
        try {
            Constructor<T> constructor = dtoClass.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.REQUIRE_NO_PARAM_CONSTRUCTOR1, dtoClass.getName()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.CONSTRUCT_FAIL2, dtoClass.getName(), BasePopulateDto.class));
        }
    }

    /**
     * 从实体对象填充当前返回值对象，如果属性值是关联的其他对象，那么也会自动从实体对象中获取对应名字的关联对象来填充返回值。
     *
     * @param entity
     * @return
     */
    public BasePopulateDto<E> populateByEntity(E entity) {
        return BasePopulateDto.populateByEntity(entity, this, this, true);
    }

    /**
     * 从实体对象填充当前返回值对象。
     *
     * @param entity
     * @param includeRelation 是否自动填充关联对象
     * @return
     */
    public BasePopulateDto<E> populateByEntity(E entity, boolean includeRelation) {
        return BasePopulateDto.populateByEntity(entity, this, this, includeRelation);
    }

    /**
     * 从实体对象创建并填充当前返回值对象，如果属性值是关联的其他对象，那么也会自动从实体对象中获取对应名字的关联对象来填充返回值。
     *
     * @param entity
     * @param dto
     * @return
     * @param <E>
     */
    public static <E extends IdPersistable> BasePopulateDto<E> populateByEntity(E entity, BasePopulateDto<E> dto) {
        return populateByEntity(entity, dto, dto, true);
    }

    /**
     * 从实体对象创建并填充当前返回值对象。
     *
     * @param entity
     * @param dto
     * @param includeRelation 是否自动填充关联对象
     * @return
     * @param <E>
     */
    public static <E extends IdPersistable> BasePopulateDto<E> populateByEntity(E entity, BasePopulateDto<E> dto, boolean includeRelation) {
        return populateByEntity(entity, dto, dto, includeRelation);
    }

    /**
     * 从实体对象填充属性至返回值对象，如果属性值是其他对象的集合，那么也会自动从实体对象中获取对应名字的集合来填充返回值的集合
     *
     * @param entity
     * @param dto
     * @param grandParentDto  to avoid infinite relation loop.
     * @param includeRelation
     * @return
     */
    public static <E extends IdPersistable> BasePopulateDto<E> populateByEntity(E entity, BasePopulateDto<E> dto, BasePopulateDto<E> grandParentDto, boolean includeRelation) {
        if (entity == null) {
            throw new RuntimeException(Info.get(BasePopulateDto.class, R.REQUIRE_ENTITY));
        }
        Logger log = LoggerFactory.getLogger(BasePopulateDto.class);
        if (log.isTraceEnabled()) log.trace(Info.get(BasePopulateDto.class, R.POPULATE_FROM_ENTITY1, entity));

        List<String> ignoredFieldNameList = new LinkedList<>();// 需要忽略的目标属性名称
        if (includeRelation) {
            /*
             * 先处理一对多关联（保证ID属性先被处理，后续处理时略过这些字段）
             */
            List<Field> fieldsByType = BeanUtils.getDeclaredFieldsByType(entity, IdPersistable.class);
            for (Field srcField : fieldsByType) {
                String relationFieldNameInDtoClass = srcField.getName() + "Id";
                try {
                    Field targetField = dto.getClass().getDeclaredField(relationFieldNameInDtoClass);
                    if (targetField.getAnnotation(JsonIgnore.class) != null
                            || targetField.getAnnotation(PopulateIgnore.class) != null) {
                        continue;
                    }

                    IdPersistable parentEntity = (IdPersistable) BeanUtils.forceGetProperty(entity, srcField);
                    if (parentEntity != null) {
                        BeanUtils.forceSetProperty(dto, relationFieldNameInDtoClass, parentEntity.getId());
                        ignoredFieldNameList.add(relationFieldNameInDtoClass); // 记录目标属性名称
                    }
                } catch (Exception e) {
                    // 忽略处理
                    continue;
                }
            }
        }
        /*
         * 处理（一对一或者多对一）关联的 DTO 对象
         */
        List<Field> fieldsN2One = BeanUtils.getDeclaredFieldsByTypeIgnore(dto, BasePopulateDto.class, JsonIgnore.class, PopulateIgnore.class);
        for (Field targetField : fieldsN2One) {
            ignoredFieldNameList.add(targetField.getName());
            if (includeRelation) {
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

                Object oneEntity = BeanUtils.forceGetProperty(entity, srcField);
                if (oneEntity instanceof IdPersistable subPersistable) {
                    Class oneDtoClass = (Class) targetField.getGenericType();
                    if (grandParentDto.getClass() != oneDtoClass) {
                        BasePopulateDto<E> oneDto = constructDto(oneDtoClass);
                        BasePopulateDto.populateByEntity((E) subPersistable, oneDto, grandParentDto, includeRelation);
                        BeanUtils.forceSetProperty(dto, targetField, oneDto);
                    }
                    else {
                        System.out.println("Loop for one, break it");
                        //break the loop
                    }
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
            // unlock if un-accessible
            boolean accessible = targetField.isAccessible();
            targetField.setAccessible(true);
            // 处理集合类属性
            if (Collection.class.isAssignableFrom(srcField.getType())
                    && Collection.class.isAssignableFrom(targetField.getType())) {
                try {
                    Type[] actualTypeArguments = ((ParameterizedType) targetField.getGenericType()).getActualTypeArguments();
                    if (actualTypeArguments.length > 0 && includeRelation) {
                        Collection<?> subEntities = (Collection<?>) BeanUtils.forceGetProperty(entity, srcField.getName());
                        if (subEntities != null && !subEntities.isEmpty()) {
                            Collection subDtos = (Collection) BeanUtils.forceGetProperty(dto, targetField.getName());
                            Class elementClass = (Class) actualTypeArguments[0]; // target element type

                            if (grandParentDto.getClass() != elementClass) {
                                if (subDtos == null) {
                                    if (Set.class.isAssignableFrom(targetField.getType())) {
                                        // 如果集合为 TreeSet 并且其中的元素的类型实现了 Comparable 接口，那么返回 TreeSet
                                        if (TreeSet.class.isAssignableFrom(targetField.getType())
                                                && Comparable.class.isAssignableFrom((Class<?>) elementClass)) {
                                            subDtos = new TreeSet<>();
                                        }
                                        else {
                                            subDtos = new HashSet<>();
                                        }
                                    }
                                    else if (List.class.isAssignableFrom(targetField.getType())) {
                                        subDtos = new LinkedList<>();
                                    }
                                    targetField.set(dto, subDtos);
                                }

                                for (Object subEntity : subEntities) {
                                    if (subEntity instanceof IdPersistable subPersistable) {
                                        BasePopulateDto<E> subDto = constructDto(elementClass);
                                        BasePopulateDto.populateByEntity((E) subPersistable, subDto, grandParentDto, includeRelation);
                                        subDtos.add(subDto);
                                    }
                                }
                            }
                            else {
                                System.out.println("Loop for many, break it");
                                // break the loop
                            }
                        }
                    }
                    else {
                        log.debug("Ignore relation field %s".formatted(targetField.getName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(Info.get(BasePopulateDto.class, R.POPULATE_COLLECTION_FAIL1, srcField.getName()));
                }
            }
            // 处理其他非集合类型
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
