package org.swiftboot.data.model.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.Info;
import org.swiftboot.data.R;
import org.swiftboot.data.config.SwiftBootDataConfigBean;
import org.swiftboot.data.model.entity.IdPersistable;
import org.swiftboot.data.model.id.IdPopulator;

import jakarta.annotation.Resource;

/**
 * 拦截 Spring Data JPA 的保存（{@code save()}）动作，设置实体类的 ID 包括所有子类的 ID
 * 如果实体类 ID 已经存在，则不做处理。
 * 这个动作在创建新实体或者保存新的子实体（仅{@code save()}之前没有flush的情况下）时起作用。
 *
 * @author swiftech
 **/
@Aspect
public class EntityIdAspect {

    private static final Logger log = LoggerFactory.getLogger(EntityIdAspect.class);

    @Resource
    private SwiftBootDataConfigBean dataConfigBean;

    @Resource
    private IdPopulator idPopulater;


    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) log.debug("%s executed before save()".formatted(this.getClass().getSimpleName()));
        // 检测前置条件
        if (!dataConfigBean.getModel().isAutoGenerateId()) {
            return null;
        }
        if (idPopulater == null) {
            return null;
        }
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return null;
        }

        for (Object arg : args) {
            if (log.isTraceEnabled()) log.trace("saving %s".formatted(arg));
            if (arg instanceof IdPersistable idPersistable) { // for saving single entity
                idPopulater.populate(idPersistable, true);
            }
            else if (arg instanceof Iterable it) { // for saving entities
                for (Object o : it) {
                    if (o instanceof IdPersistable subIdPersistable) {
                        idPopulater.populate(subIdPersistable, true);
                    }
                }
            }
            else {
                if (log.isDebugEnabled())
                    log.debug(Info.get(EntityIdAspect.class, R.PARAM_NOT_IMPLEMENT_INTERFACE2, arg, IdPersistable.class.getName()));
            }
        }
        return null;
    }

}
