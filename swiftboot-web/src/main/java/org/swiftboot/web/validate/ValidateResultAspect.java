package org.swiftboot.web.validate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.swiftboot.web.exception.ValidationException;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 将验证结果转换为JSON格式，包含原始的 key 对应 的错误消息
 *
 * @author swiftech
 */
@Aspect
@Component
public class ValidateResultAspect {
    private static Logger logger = getLogger(ValidateResultAspect.class);

    /**
     * 切入带有 ConvertValidateResult 注解的方法
     */
    @Pointcut("@annotation(org.swiftboot.web.validate.ConvertValidateResult)")
    private void convertResult() {
    }

    @Before("convertResult()")
    public void process(JoinPoint joinPoint) {
        logger.debug("@ConvertValidateResult ...");
        Object[] args = joinPoint.getArgs();
        Object result = getFirstMatch(args, BindingResult.class);
        if (result != null) {
            BindingResult bindingResult = (BindingResult) result;
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if (!allErrors.isEmpty()) {
                ValidationResult validationResult = ValidationResult.fromBindingResult(bindingResult.getTarget(), bindingResult);
                throw new ValidationException("参数验证不通过", validationResult);
            }
        }
        else {
            logger.debug("没有验证错误");
        }
    }

    /**
     * 获取数组中第一个类型匹配的元素
     * TODO 重构到 swiftboot-collections
     * @param collection
     * @param clazz
     * @return
     */
    public static Object getFirstMatch(Object[] collection, Class clazz) {
        if (collection == null || clazz == null) {
            return null;
        }
        for (Object o : collection) {
            if (clazz.isAssignableFrom(o.getClass())) {
                return o;
            }
        }
        return null;
    }

}
