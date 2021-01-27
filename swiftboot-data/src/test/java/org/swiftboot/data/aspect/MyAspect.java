package org.swiftboot.data.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author swiftech 2019-04-18
 **/
@Aspect
public class MyAspect {
    @Pointcut(value = "execution(public * org.swiftboot.data.aspect.AspectTarget.*(..))")
    public void pointcut() {
    }


    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        System.out.println("Aspectj 在 Spring Boot Test 中可以工作");
        return null;
    }

}
