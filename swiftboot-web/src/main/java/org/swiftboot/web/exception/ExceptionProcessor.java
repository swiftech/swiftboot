package org.swiftboot.web.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseCode;

import java.util.stream.Collectors;

/**
 * 应用到所有 @RequestMapping 注解的方法，在其抛出异常的时候执行。
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
     * 自定义的 ErrMessageException 异常处理，通过错误代码自动填充多语言的错误信息。
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
        // use message directly if provided
        if (StringUtils.isNotBlank(e.getMessage())) {
            return new Response<>(e);
        }
        else {
            // try to retrieve message by error code.
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
     * 数据层抛出的异常统一封装，避免暴露底层错误信息。
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseBody
    public Response<?> onDataAccessException(NativeWebRequest request, DataAccessException e) {
        log.debug("on onDataAccessException...");
        log.error(e.getMessage(), e);
        return Response.builder().code(ResponseCode.CODE_SYS_DB_ERROR).build();
    }

    /**
     * Request 对象中的枚举参数不符合时抛出的异常。
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public Response<?> onMethodArgumentNotValidException(NativeWebRequest request, HttpMessageNotReadableException e) {
        log.debug("on onMethodArgumentNotValidException...");
        log.error(e.getMessage(), e);
        if (e.getRootCause() instanceof InvalidFormatException ife) {
            String fieldName = ife.getPath().stream()
                    .map(JsonMappingException.Reference::getFieldName)
                    .collect(Collectors.joining("."));
            return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR_PARAM).messageArgs(fieldName).build();
        }
        return Response.builder().code(ResponseCode.CODE_ARGUMENTS_ERROR_PARAM).message(e.getLocalizedMessage()).build();
    }

    /**
     * 除了以上特定的异常外的异常，统一返回系统错误。
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
