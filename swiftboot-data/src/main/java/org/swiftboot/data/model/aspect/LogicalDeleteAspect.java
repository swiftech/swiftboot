package org.swiftboot.data.model.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.swiftboot.data.SwiftBootDataConfigBean;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

/**
 * 持久化实体类之前设置逻辑删除字段
 *
 * @author swiftech
 **/
@Aspect
public class LogicalDeleteAspect {

    private final Logger log = LoggerFactory.getLogger(LogicalDeleteAspect.class);

    @Resource
    private EntityManager entityManager;

    @Resource
    private SwiftBootDataConfigBean configBean;

    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.delete*(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {

        return null;
    }

}
