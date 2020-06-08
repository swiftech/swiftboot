package org.swiftboot.shiro.realm;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;

import java.io.Serializable;

/**
 * 处理 Shiro 框架 抛出的异常
 *
 * @author swiftech
 * @since 1.2
 **/
@ControllerAdvice
@Order(200)
public class ShiroExceptionProcessor {

    private static Logger log = LoggerFactory.getLogger(ShiroExceptionProcessor.class);

    /**
     * 处理 Shiro 鉴权失败的异常 {@link UnauthorizedException}
     *
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public HttpResponse<Serializable> onUnauthorizedException(
            NativeWebRequest request, UnauthorizedException e) {
        log.debug("onUnauthorizedException...");
        log.error(e.getMessage(), e);
        return new HttpResponse<>(ErrorCodeSupport.CODE_NO_PERMISSION);
    }

    /**
     * 处理 Shiro 认证失败 {@link UnauthenticatedException}
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public HttpResponse<Serializable> onUnauthenticatedException(
            NativeWebRequest request, UnauthenticatedException e) {
        log.debug("onUnauthenticatedException...");
        log.error(e.getMessage(), e);
        return new HttpResponse<>(ErrorCodeSupport.CODE_NO_SIGNIN);
    }


}
