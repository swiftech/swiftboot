package org.swiftboot.web.validate;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.swiftboot.collections.ArrayUtils;
import org.swiftboot.web.exception.ValidationException;

import java.util.List;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * 将验证结果转换为 JSON 格式，包含原始的 key 对应 的错误消息
 * 在控制器方法有 {@link BindingResult} 参数的时候有效。
 *
 * @author swiftech
 */
@Aspect
@Component
public class ValidateResultAspect {
    private static final Logger log = getLogger(ValidateResultAspect.class);

    /**
     * 切入带有 {@link ConvertValidateResult} 注解的方法
     */
    @Pointcut("@annotation(org.swiftboot.web.validate.ConvertValidateResult)")
    private void annotatedMethod() {
    }

    /**
     * 切入带有 {@link ConvertValidateResult} 注解的类
     */
    @Pointcut("@within(org.swiftboot.web.validate.ConvertValidateResult)")
    private void annotatedClass() {
    }

    @Before("annotatedMethod() || annotatedClass()")
    public void process(JoinPoint joinPoint) {
        log.trace("validate arguments ...");
        Object[] args = joinPoint.getArgs();

        if (args.length == 0 || org.apache.commons.lang3.ArrayUtils.contains(args, null)) {
            log.debug("No arguments to be validated.");
        }
        else {
            Object result = ArrayUtils.getFirstMatch(args, BindingResult.class);
            if (result == null) {
                log.debug("No arguments to be validated.");
            }
            else {
                BindingResult bindingResult = (BindingResult) result;
                log.info(Objects.requireNonNull(bindingResult.getTarget()).toString());
                List<ObjectError> allErrors = bindingResult.getAllErrors();
                if (!allErrors.isEmpty()) {
                    ValidationResult validationResult =
                            ValidationResult.readFromBindingResult(bindingResult.getTarget(), bindingResult);
                    throw new ValidationException("Arguments validation failed", validationResult);
                }
            }
        }
    }

}
