package org.swiftboot.web.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.util.BeanUtils;
import org.swiftboot.web.Info;
import org.swiftboot.web.R;
import org.swiftboot.web.SwiftBootConfigBean;
import org.swiftboot.web.model.entity.BaseIdEntity;
import org.swiftboot.web.model.entity.IdPojo;
import org.swiftboot.web.model.id.IdGenerator;

import javax.annotation.Resource;
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

    private Logger log = LoggerFactory.getLogger(EntityIdAspect.class);

    @Resource
    private SwiftBootConfigBean swiftBootConfigBean;

    @Resource
    private IdGenerator<IdPojo> idGenerator;


    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        log.debug(this.getClass().getSimpleName() + " before()");
        // 检测前置条件
        if (!swiftBootConfigBean.getModel().isAutoGenerateId()) {
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
            if (arg instanceof IdPojo) {
                IdPojo idEntity = (IdPojo) arg;
                this.tryToSetIdAndSubIds(idEntity);
            }
            else if (arg instanceof Iterable) {
                for (Object idEntity : ((Iterable) arg)) {
                    if (idEntity instanceof IdPojo) {
                        this.tryToSetIdAndSubIds((IdPojo) idEntity);
                    }
                }
            }
            else {
                log.debug(Info.get(EntityIdAspect.class, R.PARAM_NOT_IMPLEMENT_INTERFACE2, IdPojo.class.getName(), arg));
            }
        }
        return null;
    }

    /**
     * 尝试设置 ID 和所有子对象的 ID
     *
     * @param idEntity
     */
    private void tryToSetIdAndSubIds(IdPojo idEntity) {
        // ID不存在才生成
        if (StringUtils.isBlank(idEntity.getId())) {
            String id = idGenerator.generate(idEntity);
            idEntity.setId(id);
        }

        tryToSetRelEntities(idEntity, OneToOne.class);

        tryToSetRelEntities(idEntity, OneToMany.class);
    }

    /**
     * 尝试设置关联实体类的 ID
     *
     * @param parentEntity
     * @param clazz
     */
    private void tryToSetRelEntities(IdPojo parentEntity, Class<? extends Annotation> clazz) {
        List<Field> otoList = FieldUtils.getFieldsListWithAnnotation(parentEntity.getClass(), clazz);
        for (Field oto : otoList) {
            Object relEntity = BeanUtils.forceGetProperty(parentEntity, oto);
            if (relEntity instanceof BaseIdEntity) {
                tryToSetIdAndSubIds((BaseIdEntity) relEntity); // - 递归 -
            }
            else if (relEntity instanceof Iterable) {
                for (Object subEntity : ((Iterable) relEntity)) {
                    tryToSetIdAndSubIds((BaseIdEntity) subEntity); // - 递归 -
                }
            }
        }
    }

}
