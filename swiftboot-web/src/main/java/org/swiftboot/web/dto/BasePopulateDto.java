package org.swiftboot.web.dto;

import org.swiftboot.data.model.entity.IdPersistable;

/**
 * <code>populateByEntity(E entity, boolean includeRelation)</code> 方法将实体对象的属性填充到当前对象实例中，includeRelation 提供了是否也要填充关联的 DTO 类。
 * <code>populateByEntity(E entity)</code> 方法将实体对象的属性填充到当前对象实例中，包含所有子 DTO 对象。
 *
 * @author swiftech
 **/
public abstract class BasePopulateDto<E extends IdPersistable> implements PopulatableDto<E> {

    /**
     * 从实体对象填充当前返回值对象，如果属性值是关联的其他对象，那么也会自动从实体对象中获取对应名字的关联对象来填充返回值。
     *
     * @param entity
     * @return
     */
    public PopulatableDto<E> populateByEntity(E entity) {
        return PopulatableDto.populateByEntity(entity, this, this, true);
    }

    /**
     * 从实体对象填充当前返回值对象。
     *
     * @param entity
     * @param includeRelation 是否自动填充关联对象
     * @return
     */
    public PopulatableDto<E> populateByEntity(E entity, boolean includeRelation) {
        return PopulatableDto.populateByEntity(entity, this, this, includeRelation);
    }

}
