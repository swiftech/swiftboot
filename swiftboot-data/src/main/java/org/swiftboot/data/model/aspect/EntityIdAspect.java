package org.swiftboot.data.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.Info;
import org.swiftboot.data.R;
import org.swiftboot.data.SwiftBootDataConfigBean;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.IdGenerator;
import org.swiftboot.util.BeanUtils;

import javax.annotation.Resource;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * 拦截 Spring Data JPA 的保存动作，设置实体类的 ID 包括所有子类的 ID
 * 如果实体类 ID 已经存在，则不做处理
 *
 * @author swiftech
 **/
@Aspect
public class EntityIdAspect {

    private final Logger log = LoggerFactory.getLogger(EntityIdAspect.class);

    @Resource
    private SwiftBootDataConfigBean dataConfigBean;

    @Resource
    private IdGenerator<IdPersistable> idGenerator;


    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        log.debug(this.getClass().getSimpleName() + " before()");
        // 检测前置条件
        if (!dataConfigBean.getModel().isAutoGenerateId()) {
            return null;
        }
        if (idGenerator == null) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            if (arg instanceof IdPersistable) { // for saving single entity
                IdPersistable idEntity = (IdPersistable) arg;
                this.tryToSetIdAndSubIds(idEntity);
            }
            else if (arg instanceof Iterable) { // for saving entities
                for (Object idEntity : ((Iterable) arg)) {
                    if (idEntity instanceof IdPersistable) {
                        this.tryToSetIdAndSubIds((IdPersistable) idEntity);
                    }
                }
            }
            else {
                log.debug(Info.get(EntityIdAspect.class, R.PARAM_NOT_IMPLEMENT_INTERFACE2, arg, IdPersistable.class.getName()));
            }
        }
        return null;
    }

    /**
     * 尝试设置 ID 和所有子对象的 ID
     *
     * @param idEntity
     */
    private void tryToSetIdAndSubIds(IdPersistable idEntity) {
        // ID不存在才生成
        if (StringUtils.isBlank(idEntity.getId())) {
            String id = idGenerator.generate(idEntity);
            idEntity.setId(id);
        }

        this.tryToSetOneToOneEntities(idEntity);
        this.tryToSetOneToManyEntities(idEntity);
    }

    /**
     * 尝试设置关联实体类的 ID
     *
     * @param parentEntity
     * @param clazz
     * @deprecated
     */
    private void tryToSetRelEntities(IdPersistable parentEntity, Class<? extends Annotation> clazz) {
        List<Field> subObjectList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), clazz);
        for (Field subObject : subObjectList) {
            Object relEntity = BeanUtils.forceGetProperty(parentEntity, subObject);
            if (relEntity instanceof IdPersistable) {
                tryToSetIdAndSubIds((IdPersistable) relEntity); // - 递归 -
            }
            else if (relEntity instanceof Iterable) {
                for (Object subEntity : ((Iterable) relEntity)) {
                    tryToSetIdAndSubIds((IdPersistable) subEntity); // - 递归 -
                }
            }
        }
    }

    /**
     * Try to set id of one to one relation.
     *
     * @param parentEntity
     */
    private void tryToSetOneToOneEntities(IdPersistable parentEntity) {
        List<Field> subObjectList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), OneToOne.class);
        for (Field subObjectField : subObjectList) {
            Object relEntity = BeanUtils.forceGetProperty(parentEntity, subObjectField);
            if (relEntity == null) continue;
            tryToSetIdAndSubIds((IdPersistable) relEntity);
        }
    }

    /**
     * Try to set id of one to many relation
     *
     * @param parentEntity
     */
    private void tryToSetOneToManyEntities(IdPersistable parentEntity) {
        List<Field> subObjectList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), OneToMany.class);
        for (Field subObjectField : subObjectList) {
            Object relEntity = BeanUtils.forceGetProperty(parentEntity, subObjectField);
            if (relEntity == null) continue;
            for (Object subEntity : ((Iterable<?>) relEntity)) {
                tryToSetIdAndSubIds((IdPersistable) subEntity); // - 递归 -
                // 反向处理 ManyToOne 的属性
                List<Field> m2oFields = FieldUtils.getFieldsListWithAnnotation(subEntity.getClass(), ManyToOne.class);
                for (Field m2oField : m2oFields) {
                    if (m2oField.getType() == parentEntity.getClass()) {
                        BeanUtils.forceSetProperty(subEntity, m2oField, parentEntity);
                    }
                }
            }
        }
    }

}
