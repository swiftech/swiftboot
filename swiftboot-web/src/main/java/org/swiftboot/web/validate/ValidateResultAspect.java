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
 * 在控制器方法有 BindingResult 参数的时候
 *
 * @author swiftech
 */
@Aspect
@Component
public class ValidateResultAspect {
    private static Logger log = getLogger(ValidateResultAspect.class);

    /**
     * 切入带有 ConvertValidateResult 注解的方法
     */
    @Pointcut("@annotation(org.swiftboot.web.validate.ConvertValidateResult)")
    private void convertResult() {
    }

//    @Pointcut("@annotation(org.swiftboot.web.validate.ConvertValidateResult)")
//    private void convertResult() {
//    }

    @Before("convertResult()")
    public void process(JoinPoint joinPoint) {
        log.debug("@ConvertValidateResult ...");
        Object[] args = joinPoint.getArgs();
        Object result = ArrayUtils.getFirstMatch(args, BindingResult.class);
        if (result != null) {
            BindingResult bindingResult = (BindingResult) result;
            log.info(Objects.requireNonNull(bindingResult.getTarget()).toString());
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if (!allErrors.isEmpty()) {
                ValidationResult validationResult =
                        ValidationResult.readFromBindingResult(bindingResult.getTarget(), bindingResult);
                throw new ValidationException("参数验证不通过", validationResult);
            }
        }
        else {
            log.debug("没有验证错误");
        }
    }

}
