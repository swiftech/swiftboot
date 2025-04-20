package org.swiftboot.auth.interceptor;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.swiftboot.auth.annotation.*;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.service.JwtService;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.JwtUtils;

/**
 * Populate values from JWT to the annotated parameter of controller.
 * {@link UserId}, {@link UserName}, {@link ExpireTime}, {@link Addition}
 * this argument resolver only works under JWT mode.
 *
 * @see JwtService
 * @see org.swiftboot.auth.service.AuthenticatedResponse
 * @since 3.0
 */
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger log = LoggerFactory.getLogger(JwtArgumentResolver.class);

    @Resource
    private AuthConfigBean configBean;

    @Resource
    private JwtService jwtService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UserId.class)
                || parameter.hasParameterAnnotation(UserName.class)
                || parameter.hasParameterAnnotation(ExpireTime.class)
                || parameter.hasParameterAnnotation(Addition.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest;
        String bearerToken = servletWebRequest.getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = JwtUtils.extractBearerToken(bearerToken);
        if (StringUtils.isBlank(accessToken)) {
            if (log.isTraceEnabled()) log.trace("No access token found in headers");
            return null;
        }
        else {
            if (parameter.hasParameterAnnotation(UserId.class)) {
                return jwtTokenProvider.getUserId(accessToken);
            }
            else if (parameter.hasParameterAnnotation(UserName.class)) {
                return jwtTokenProvider.getUsername(accessToken);
            }
            else if (parameter.hasParameterAnnotation(ExpireTime.class)) {
                return jwtTokenProvider.getExpireTime(accessToken).getTime();
            }
            else if (parameter.hasParameterAnnotation(Addition.class)) {
                Addition anno = parameter.getParameterAnnotation(Addition.class);
                if (anno != null && StringUtils.isNotBlank(anno.value()))
                    return jwtTokenProvider.getAddition(accessToken, anno.value());
                else return null;
            }
            else if (parameter.hasParameterAnnotation(Session.class)) {
                throw new RuntimeException("Not supported");
            }
            else
                return null;
        }
    }
}
