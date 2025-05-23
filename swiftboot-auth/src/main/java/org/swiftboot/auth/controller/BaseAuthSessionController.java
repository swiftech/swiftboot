package org.swiftboot.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;
import org.swiftboot.auth.config.AuthConfigBean;
import org.swiftboot.auth.model.Session;
import org.swiftboot.auth.service.SessionService;
import org.swiftboot.web.exception.ErrMessageException;
import org.swiftboot.web.response.ResponseCode;

import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 继承的子类可以用 {@code fetchUserIdFromSession()} 方法快速的获取用户会话中保存的用户 ID
 *
 * @author swiftech
 **/
public class BaseAuthSessionController {

    protected static final Logger log = LoggerFactory.getLogger(BaseAuthSessionController.class);

    @Resource
    protected AuthConfigBean authConfigBean;

    @Resource
    protected SessionService sessionService;

    /**
     * 通过参数 token 获取会话中保存的用户 ID
     * 如果会话无效则抛出异常
     *
     * @param token
     * @return
     */
    public String fetchUserIdFromSession(String token) {
        if (StringUtils.isBlank(token)) {
            throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN, "Token is not provided for this request");
        }
        Session session = sessionService.getSession(token);
        if (session != null) {
            String userId = session.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN, String.format("User ID not exist in session: %s", token));
            }
            else {
                return userId;
            }
        }
        else {
            throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN, String.format("User session not exist: %s", token));
        }
    }

    /**
     * 通过请求的 header 或者 cookie 中获取会话中保存的用户 ID
     * 如果会话无效则抛出异常
     *
     * @param request
     * @return
     */
    public String fetchUserIdFromSession(HttpServletRequest request) {
        String tokenKey = authConfigBean.getTokenKey();
        String token = request.getHeader(tokenKey);
        if (StringUtils.isBlank(token)) {
            Cookie cookie = WebUtils.getCookie(request, tokenKey);
            if (cookie != null) {
                token = cookie.getValue();
                if (StringUtils.isBlank(token)) {
                    throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN,
                            String.format("Token '%s' is not provided neither in header nor in cookie for this request", tokenKey));
                }
            }
            else {
                throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN,
                        String.format("Token '%s' is not provided neither in header nor in cookie for this request", tokenKey));
            }
        }

        Session session = sessionService.getSession(token);
        if (session != null) {
            String userId = session.getUserId();
            if (StringUtils.isBlank(userId)) {
                throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN, String.format("User ID not exist in session: %s", token));
            }
            else {
                return userId;
            }
        }
        else {
            throw new ErrMessageException(ResponseCode.CODE_NO_SIGNIN, String.format("User session not exist: %s", token));
        }
    }

}
