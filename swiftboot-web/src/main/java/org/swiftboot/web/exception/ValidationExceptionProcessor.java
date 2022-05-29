package org.swiftboot.web.exception;

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
import org.swiftboot.web.validate.ValidationResult.InputError;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 处理 Validation 框架和 SwiftBoot 抛出的验证异常
 *
 * @author swiftech
 **/
@ControllerAdvice
@Order(100)
public class ValidationExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(ValidationExceptionProcessor.class);

    @Resource
    SwiftBootWebConfigBean swiftBootConfigBean;

    /**
     * 处理 Validation 框架抛出的验证异常，在 {@link org.swiftboot.web.validate.ValidateResultAspect} 不生效的时候(在控制器方法没有 BindingResult 参数的时候)做处理。
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
                return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, validationResult);
            }
            else {
                StringBuilder buf = new StringBuilder();
                if (validationResult != null && !validationResult.isEmpty()) {
                    for (InputError inputError : validationResult) {
                        buf.append(inputError.getMsg()).append(" ");
                    }
                }
                return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, buf.toString().trim());
            }
        }
        return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR);
    }


    /**
     * 应用到所有 @RequestMapping 注解的方法,在其抛出 ValidationException 的时候执行
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
            return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, e.getValidationResult());
        }
        else {
            StringBuilder buf = new StringBuilder();
            for (InputError inputError : e.getValidationResult()) {
                buf.append(inputError.getMsg()).append(" ");
            }
            return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, buf.toString().trim());
        }
    }
}
