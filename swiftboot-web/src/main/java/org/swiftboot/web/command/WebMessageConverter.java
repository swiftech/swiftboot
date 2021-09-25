package org.swiftboot.web.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 扩展 SpringMVC 的接口参数转换，默认情况下会被启用。
 * 包括：将 HTTP 头写入 HttpCommand 对象
 *
 * @author swiftech
 * @see HttpCommand
 * @deprecated use {@link org.swiftboot.web.interceptor.WebMessageAdvice} instead.
 **/
public class WebMessageConverter extends MappingJackson2HttpMessageConverter {

    public WebMessageConverter() {
    }

    public WebMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Object converted = super.read(type, contextClass, inputMessage);
        if (converted instanceof HttpCommand) {
            HttpHeaders headers = inputMessage.getHeaders();
            if (headers != null && !headers.isEmpty()) {
                for (String hname : headers.keySet()) {
                    List<String> hValues = headers.getValuesAsList(hname);
                    if (hValues != null && !hValues.isEmpty()) {
                        ((HttpCommand) converted).setHeader(hname, hValues.get(0));
                    }
                }
            }
        }
        return converted;
    }
}
