package org.swiftboot.web.model.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.swiftboot.web.SwiftBootConfigBean;
import org.swiftboot.web.model.entity.BaseIdEntity;
import org.swiftboot.web.model.id.IdGenerator;

import javax.annotation.Resource;

/**
 * 拦截 Spring Data JPA 的保存动作，设置实体类的 ID
 * @author swiftech
 **/
@Aspect
public class EntityIdAspect {

    @Resource
    SwiftBootConfigBean swiftBootConfigBean;

    @Resource
    private IdGenerator<BaseIdEntity> idGenerator;

    @Pointcut(value = "execution(public * org.springframework.data.repository.CrudRepository+.save(..))")
    public void pointcut() {
    }

    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        if (!swiftBootConfigBean.getModel().isAutoGenerateId()) {
            return null;
        }
        System.out.println("aop:before()");

        for (Object arg : joinPoint.getArgs()) {
            System.out.println(arg.getClass());
        }

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            throw new RuntimeException("");
        }

        if (args[0] instanceof BaseIdEntity) {
            BaseIdEntity idEntity = (BaseIdEntity) args[0];

            if (idGenerator == null) {
                return null;
            }
            String id = idGenerator.generate(idEntity);
            System.out.println("ID: " + id);
            idEntity.setId(id);
        }
        else {
            System.out.println("不是带有ID的实体类: " + joinPoint.getTarget());
        }
        return null;
    }
}
