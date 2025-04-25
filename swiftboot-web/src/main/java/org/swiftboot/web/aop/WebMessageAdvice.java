package org.swiftboot.web.aop;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;
import org.swiftboot.web.command.HttpCommand;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 扩展 SpringMVC 的接口参数转换，默认情况下会被启用。
 * 包括：将 HTTP 头写入 HttpCommand 对象
 *
 * @author swiftech
 * @see HttpCommand
 * @since 2.1
 */
@ControllerAdvice
public class WebMessageAdvice extends RequestBodyAdviceAdapter {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return targetType == HttpCommand.class || HttpCommand.class.isAssignableFrom((Class<?>) targetType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpHeaders headers = inputMessage.getHeaders();
        if (!headers.isEmpty()) {
            for (String hname : headers.keySet()) {
                List<String> hValues = headers.getValuesAsList(hname);
                if (!hValues.isEmpty()) {
                    ((HttpCommand) body).setHeader(hname, hValues.get(0));
                }
            }
        }
        return body;
    }
}
