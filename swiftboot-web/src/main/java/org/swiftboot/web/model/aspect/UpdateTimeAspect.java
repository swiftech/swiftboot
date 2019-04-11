package org.swiftboot.web.model.aspect;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.web.model.entity.BaseEntity;

/**
 * 保存实体类之前设置更新时间（UPDATE_TIME）
 *
 * @author swiftech
 **/
@Aspect
public class UpdateTimeAspect {

    private Logger log = LoggerFactory.getLogger(EntityIdAspect.class);

    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        // 检测前置条件
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            return null;
        }

        for (Object arg : args) {
            if (arg instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) arg;
                if (StringUtils.isNotBlank(baseEntity.getId())) {
                    baseEntity.setUpdateTime(System.currentTimeMillis());
                }
            }
            else {
                log.warn("不是继承自 BaseEntity 的实体类: " + arg);
            }
        }
        return null;
    }
}
