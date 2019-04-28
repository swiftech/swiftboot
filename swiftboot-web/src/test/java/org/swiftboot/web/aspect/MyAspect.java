package org.swiftboot.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author Allen 2019-04-18
 **/
@Aspect
public class MyAspect {
    @Pointcut(value = "execution(public * org.swiftboot.web.aspect.AspectTarget.*(..))")
    public void pointcut() {
    }


    @Before(value = "pointcut()")
    public Object before(JoinPoint joinPoint) {
        System.out.println("Aspectj 在 Spring Boot Test 中可以工作");
        return null;
    }

}
