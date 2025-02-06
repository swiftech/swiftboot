package org.swiftboot.web.exception;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.config.SwiftBootWebConfigBean;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.validate.ValidationResult;

import jakarta.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理 Validation 框架和 SwiftBoot 抛出的验证异常
 *
 * @author swiftech
 * @see org.swiftboot.web.validate.ValidateResultAspect
 * @see ValidationResult
 **/
@ControllerAdvice
@Order(100)
public class ValidationExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(ValidationExceptionProcessor.class);

    @Resource
    private SwiftBootWebConfigBean swiftBootConfigBean;

    @Resource
    private ErrorCodeSupport errorCodeSupport;

    /**
     * 处理 Validation 框架抛出的验证异常，在 {@link org.swiftboot.web.validate.ValidateResultAspect}
     * 不生效的时候(在控制器方法没有 {@link BindingResult} 参数的时候)做处理。
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public HttpResponse<Serializable> onMethodArgumentNotValidException(
            NativeWebRequest request, MethodArgumentNotValidException e) {
        log.debug("onMethodArgumentNotValidException...");
        log.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        if (bindingResult.hasErrors()) {
            ValidationResult validationResult = ValidationResult.readFromBindingResult(bindingResult.getTarget(), bindingResult);
            if (swiftBootConfigBean.getValidation().isResultInJson()) {
                return new HttpResponse<>(ErrorCodeSupport.CODE_ARGUMENTS_ERROR, validationResult);
            }
            else {
                String msgs = this.joinInputErrorMessages(validationResult);
                return new HttpResponse<>(ErrorCodeSupport.CODE_ARGUMENTS_ERROR, msgs);
            }
        }
        return new HttpResponse<>(ErrorCodeSupport.CODE_ARGUMENTS_ERROR);
    }


    /**
     * 应用到所有 {@link org.springframework.web.bind.annotation.RequestMapping} 注解的方法,在其抛出 {@link ValidationException} 的时候执行
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public HttpResponse<ValidationResult> onValidationException(NativeWebRequest request, ValidationException e) {
        log.debug("onValidationException...");
        log.error(e.getMessage(), e);
        if (swiftBootConfigBean.getValidation().isResultInJson()) {
            return new HttpResponse<>(ErrorCodeSupport.CODE_ARGUMENTS_ERROR, e.getValidationResult());
        }
        else {
            String msgs = this.joinInputErrorMessages(e.getValidationResult());
            return new HttpResponse<>(ErrorCodeSupport.CODE_ARGUMENTS_ERROR, msgs);
        }
    }

    /**
     * Join the {@link ValidationResult.InputError} from {@link ValidationResult} to a composited message.
     * The parameterized validation error messages have already been processed in {@link ValidationResult}
     *
     * @param validationResult
     * @return
     */
    private String joinInputErrorMessages(ValidationResult validationResult) {
        if (validationResult == null) {
            return StringUtils.EMPTY;
        }
        List<String> msgs = validationResult.stream().map(ValidationResult.InputError::getMsg).collect(Collectors.toList());
        return StringUtils.join(msgs, ",");
    }
}
