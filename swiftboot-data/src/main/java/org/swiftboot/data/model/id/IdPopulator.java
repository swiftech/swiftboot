package org.swiftboot.data.model.id;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.util.BeanUtils;

import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.reflect.Field;
import java.util.List;

/**
 * Populator for generating ID and populating ID to entity and all it's one-to-one and one-to-many entities.
 *
 * @author allen
 * @since 2.0.1
 */
public class IdPopulator {

    private final Logger log = LoggerFactory.getLogger(IdPopulator.class);

    @Resource
    private IdGenerator<IdPersistable> idGenerator;

    /**
     * Populate id to entity and all it's related entities.
     *
     * @param idEntity
     */
    public void populate(IdPersistable idEntity) {
        log.trace("Try to populate entity with id");
        if (StringUtils.isBlank(idEntity.getId())) {
            log.debug("Generate ID for " + idEntity);
            String id = idGenerator.generate(idEntity);
            idEntity.setId(id);
        }

        this.tryToPopulateOneToOneEntities(idEntity);
        this.tryToPopulateOneToManyEntities(idEntity);
    }

    /**
     * Try to set id of one to one relations.
     *
     * @param parentEntity
     */
    private void tryToPopulateOneToOneEntities(IdPersistable parentEntity) {
        log.trace("Try to populate one-to-one sub-entity with id");
        List<Field> subObjectList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), OneToOne.class);
        for (Field subObjectField : subObjectList) {
            Object relEntity = BeanUtils.forceGetProperty(parentEntity, subObjectField);
            if (relEntity == null) continue;
            populate((IdPersistable) relEntity);
        }
    }

    /**
     * Try to set id of one to many relations.
     *
     * @param parentEntity
     */
    private void tryToPopulateOneToManyEntities(IdPersistable parentEntity) {
        log.trace("Try to populate one-to-many sub-entities with id");
        List<Field> subObjectList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), OneToMany.class);
        for (Field subObjectField : subObjectList) {
            Object relEntities = BeanUtils.forceGetProperty(parentEntity, subObjectField);
            if (relEntities == null) {
                log.debug("@OneToMany annotated field is null");
                continue;
            }
            for (Object subEntity : ((Iterable<?>) relEntities)) {
                if (!(subEntity instanceof IdPersistable)) {
                    continue;
                }
                populate((IdPersistable) subEntity); // - 递归 -
                // 反向处理 ManyToOne 的属性
                List<Field> m2oFields = FieldUtils.getFieldsListWithAnnotation(subEntity.getClass(), ManyToOne.class);
                for (Field m2oField : m2oFields) {
                    if (m2oField.getType() == parentEntity.getClass()) {
                        log.debug("Attach parent entity to sub entity " + subEntity);
                        BeanUtils.forceSetProperty(subEntity, m2oField, parentEntity);
                        log.debug(String.valueOf(BeanUtils.forceGetProperty(subEntity, m2oField)));
                    }
                }
            }
        }
    }

}
