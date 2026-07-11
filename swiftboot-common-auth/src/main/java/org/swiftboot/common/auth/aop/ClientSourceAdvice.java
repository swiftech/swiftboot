package org.swiftboot.common.auth.aop;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.swiftboot.common.auth.request.BaseAuthRequest;
import org.swiftboot.common.auth.request.UserContextHolder;

import java.lang.reflect.Type;

/**
 * Bind clientSource from {@link BaseAuthRequest}
 *
 * @see  BaseAuthRequest
 * @since 3.2
 */
@ControllerAdvice
public class ClientSourceAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return targetType == BaseAuthRequest.class || BaseAuthRequest.class.isAssignableFrom((Class<?>) targetType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (body instanceof BaseAuthRequest authRequest) {
            if (StringUtils.isNotBlank(authRequest.getClientSource())) {
                UserContextHolder.setClientSource(authRequest.getClientSource());
            }
        }
        return body;
    }

}
