package org.swiftboot.web.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.web.model.entity.BaseEntity;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * 保存实体类之前设置更新时间（UPDATE_TIME）
 *
 * @author swiftech
 **/
@Aspect
public class UpdateTimeAspect {

    private Logger log = LoggerFactory.getLogger(UpdateTimeAspect.class);

    @Resource
    private EntityManager entityManager;

    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        log.debug(this.getClass().getSimpleName() + " before()");
        // 检测前置条件
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            if (arg instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) arg;
                this.tryToSetUpdateTime(baseEntity);
            } else if (arg instanceof Iterable) {
                for (Object baseEntity : ((Iterable) arg)) {
                    if (baseEntity instanceof BaseEntity) {
                        this.tryToSetUpdateTime((BaseEntity) baseEntity);
                    }
                }
            } else {
                log.debug("略过不处理: " + arg);
            }
        }
        return null;
    }

    private void tryToSetUpdateTime(BaseEntity baseEntity) {
        if (StringUtils.isNotBlank(baseEntity.getId()) && entityManager.contains(baseEntity)) {
            baseEntity.setUpdateTime(System.currentTimeMillis());
        }
    }
}
