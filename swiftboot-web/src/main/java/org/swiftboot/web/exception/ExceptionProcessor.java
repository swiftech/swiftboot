package org.swiftboot.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.result.HttpResponse;

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
 * @author swiftech
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExceptionProcessor {

    private static final Logger log = LoggerFactory.getLogger(ExceptionProcessor.class);

    /**
     * 应用到所有 @RequestMapping 注解的方法,在其抛出 ErrMessageException 的时候执行
     *
     * @param request 请求参数
     * @param e       异常参数
     * @return HttpResponse 返回参数
     */
    @ExceptionHandler(ErrMessageException.class)
    @ResponseBody
    public HttpResponse<?> onErrMessageException(NativeWebRequest request, ErrMessageException e) {
        log.debug("onErrMessageException...");
        log.error(e.getMessage(), e);
        return new HttpResponse<>(e);
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
    public HttpResponse<?> onException(NativeWebRequest request, Exception e) {
        log.debug("onException...");
        log.error(e.getMessage(), e);
        return new HttpResponse<>(ErrorCodeSupport.CODE_SYS_ERR, e.getLocalizedMessage());
    }

}
