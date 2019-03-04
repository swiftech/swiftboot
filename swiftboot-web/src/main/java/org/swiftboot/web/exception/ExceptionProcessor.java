package org.swiftboot.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.validate.ValidationResult;

import java.io.Serializable;
import java.util.List;

/**
 * 项目中需添加：
 * <context:component-scan base-package="org.swiftboot.web"/>
 * <mvc:annotation-driven/>
 * 配置：
 * common.web.validation.result.json
 *
 * @author swiftech
 */
@ControllerAdvice
public class ExceptionProcessor {

    private static Logger logger = LoggerFactory.getLogger(ExceptionProcessor.class);

    @Value("${common.web.validation.result.json:true}")
    boolean isValidationResultJson = true;

    /**
     * 应用到所有 @RequestMapping 注解的方法,在其抛出 ErrMessageException 的时候执行
     *
     * @param request 请求参数
     * @param e       异常参数
     * @return HttpResponse 返回参数
     */
    @ExceptionHandler(ErrMessageException.class)
    @ResponseBody
    public HttpResponse onErrMessageException(NativeWebRequest request, ErrMessageException e) {
        logger.debug("onErrMessageException...");
        logger.error(e.getMessage(), e);
        return new HttpResponse(e);
    }

    /**
     * 应用到所有 @RequestMapping 注解的方法,在其抛出 Exception 的时候执行
     *
     * @param request 请求参数
     * @param e       异常参数
     * @return HttpResponse 返回参数
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public HttpResponse onException(NativeWebRequest request, Exception e) {
        logger.debug("onException...");
        logger.error(e.getMessage(), e);
        return new HttpResponse(ErrorCodeSupport.CODE_SYS_ERR, e.getLocalizedMessage());
    }

    /**
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public HttpResponse<ValidationResult> onValidationException(NativeWebRequest request, ValidationException e) {
        logger.debug("onValidationException...");
        logger.error(e.getMessage(), e);
        if (isValidationResultJson) {
            return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, e.getValidationResult());
        }
        else {
            StringBuffer buf = new StringBuffer();
            for (ValidationResult.InputError inputError : e.getValidationResult()) {
                buf.append(inputError.getKey()).append(":").append(inputError.getMsg()).append(" ");
            }
            return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, buf.toString().trim());
        }
    }

    /**
     * 处理验证异常（ControllerAdvise 处理异常可能先于 @Aspect 执行，所以此处加上异常处理）
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public HttpResponse<Serializable> onMethodArgumentNotValidException(NativeWebRequest request, MethodArgumentNotValidException e) {
        logger.debug("onMethodArgumentNotValidException...");
        logger.error(e.getMessage(), e);
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        if (!allErrors.isEmpty()) {
            if (isValidationResultJson) {
                ValidationResult validationResult = ValidationResult.fromBindingResult(bindingResult.getTarget(), bindingResult);
                return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, validationResult);
            }
            else {
                StringBuffer buf = new StringBuffer();
                if (bindingResult.hasErrors()) {
                    for (ObjectError objectError : bindingResult.getAllErrors()) {
                        buf.append(objectError.getCode()).append(":").append(objectError.getDefaultMessage()).append(" ");
                    }
                }
                return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR, buf.toString().trim());
            }
        }
        return new HttpResponse<>(ErrorCodeSupport.CODE_PARAMS_ERROR);
    }
}
