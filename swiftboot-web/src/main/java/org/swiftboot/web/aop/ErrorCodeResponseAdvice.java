package org.swiftboot.web.aop;

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
import org.swiftboot.web.response.ResponseCode;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.util.MessageUtils;

import jakarta.annotation.Resource;

/**
 * Populate the error message for {@link Response} with error code only.
 * The {@link Response} instance with user customized message will be ignored.
 * <p>
 * NOTE: this advice does not affect the {@link org.swiftboot.web.exception.ExceptionProcessor}
 *
 * @author swiftech
 * @see Response
 * @since 2.2
 */
@ControllerAdvice
public class ErrorCodeResponseAdvice extends AbstractMappingJacksonResponseBodyAdvice {

    private static final Logger log = LoggerFactory.getLogger(ErrorCodeResponseAdvice.class);

    @Resource
    private ResponseCode responseCode;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(returnType, converterType)
                && Response.class.isAssignableFrom(returnType.getParameterType());
    }

    @Override
    protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType, MethodParameter returnType,
                                           ServerHttpRequest request, ServerHttpResponse response) {
        if (log.isDebugEnabled()) log.debug("Handle message for HttpResponse.");
        Response<?> responseBody = (Response<?>) bodyContainer.getValue();
        String errorCode = responseBody.getCode();
        String[] msgParams = responseBody.getMsgParams();
        String msg = responseBody.getMessage();

        if (StringUtils.isBlank(msg)) {
            // not user-defined message.
            if (StringUtils.isNotBlank(errorCode)) {
                String errorMessage;
                if (ArrayUtils.isEmpty(msgParams)) {
                    errorMessage = responseCode.getMessage(errorCode);
                }
                else {
                    errorMessage = responseCode.getMessage(errorCode, msgParams);
                }
                responseBody.setMessage(errorMessage);
            }
        }
        else {
            // user-defined message, need instantiate if message parameterized.
            String errorMessage = msg;
            if (!ArrayUtils.isEmpty(msgParams)) {
                errorMessage = MessageUtils.instantiateMessage(msg, msgParams);
            }
            responseBody.setMessage(errorMessage);
        }
    }
}
