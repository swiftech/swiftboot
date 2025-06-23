package org.swiftboot.web.exception;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;

/**
 * 项目中需添加：
 * <pre>
 * &lt;context:component-scan base-package="org.swiftboot.web"/&gt;
 * &lt;mvc:annotation-driven/&gt
 * </pre>
 * 或者
 * <pre>
 * {@link org.springframework.context.annotation.ComponentScan @ComponentScan}(basePackages = {"org.swiftboot.web"})
 * </pre>
 * 配置：
 * <pre>
 * swiftboot:
 *   web:
 *     validation:
 *       resultInJson: false
 * </pre>
 *
 * @author swiftech
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExceptionProcessor.class);

    @Resource
    private ResponseCode responseCode;

    /**
     * 应用到所有 @RequestMapping 注解的方法,在其抛出 ErrMessageException 的时候执行
     *
     * @param request 请求参数
     * @param e       异常参数
     * @return HttpResponse 返回参数
     */
    @ExceptionHandler(ErrMessageException.class)
    @ResponseBody
    public Response<?> onErrMessageException(NativeWebRequest request, ErrMessageException e) {
        log.debug("on ErrMessageException...");
        log.error(e.getMessage(), e);
        if (StringUtils.isNotBlank(e.getMessage())) {
            return new Response<>(e);
        }
        else {
            String message;
            if (e.getMessageArgs() != null && !e.getMessageArgs().isEmpty()) {
                message = responseCode.getMessage(e.getErrorCode(), e.getMessageArgs().toArray(new String[0]));
            }
            else {
                message = responseCode.getMessage(e.getErrorCode());
            }
            return new Response<>(e.getErrorCode(), message);
        }
    }

    /**
     * 专门处理常见的参数错误
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    @ResponseBody
    public Response<?> onHttpMessageConversionException(NativeWebRequest request, Exception e) {
        log.debug("on HttpMessageConversionException...");
        log.error(e.getMessage(), e);
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR_PARAM).messageArgs(e.getLocalizedMessage()).build();
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
    public Response<?> onException(NativeWebRequest request, Exception e) {
        log.debug("on Exception...");
        log.error(e.getMessage(), e);
        return new Response<>(ResponseCode.CODE_SYS_ERR, e.getLocalizedMessage());
    }

}
