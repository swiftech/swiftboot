package org.swiftboot.web.interceptor;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;
import org.swiftboot.web.exception.ErrorCodeSupport;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.util.MessageUtils;

import javax.annotation.Resource;

/**
 * Populate the error message for {@link HttpResponse} with error code only.
 * The {@link HttpResponse} instance with user customized message will be ignored.
 * <p>
 * NOTE: this advice does not affect the {@link org.swiftboot.web.exception.ExceptionProcessor}
 *
 * @author swiftech
 * @see HttpResponse
 * @since 2.2
 */
@ControllerAdvice
public class ErrorCodeResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(ErrorCodeResponseAdvice.class);

    @Resource
    private ErrorCodeSupport errorCodeSupport;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && HttpResponse.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType, ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled()) log.debug("Handle message for HttpResponse.");
        HttpResponse<?> httpResponse = (HttpResponse<?>) bodyContainer.getValue();
        String errorCode = httpResponse.getCode();
        String[] msgParams = httpResponse.getMsgParams();
        String msg = httpResponse.getMsg();

        if (StringUtils.isBlank(msg)) {
            // not user-defined message.
            if (StringUtils.isNotBlank(errorCode)) {
                String errorMessage;
                if (ArrayUtils.isEmpty(msgParams)) {
                    errorMessage = errorCodeSupport.getMessage(errorCode);
                }
                else {
                    errorMessage = errorCodeSupport.getMessage(errorCode, msgParams);
                }
                httpResponse.setMsg(errorMessage);
            }
        }
        else {
            // user-defined message, need instantiate if message parameterized.
            String errorMessage = msg;
            if (!ArrayUtils.isEmpty(msgParams)) {
                errorMessage = MessageUtils.instantiateMessage(msg, msgParams);
            }
            httpResponse.setMsg(errorMessage);
        }
    }
}
