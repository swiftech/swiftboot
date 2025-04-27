package org.swiftboot.demo.handler;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.swiftboot.common.auth.token.AccessToken;
import org.swiftboot.common.auth.JwtTokenProvider;
import org.swiftboot.common.auth.token.RefreshToken;
import org.swiftboot.demo.dto.AuthenticatedDto;
import org.swiftboot.demo.dto.UserInfoDto;
import org.swiftboot.demo.service.UserService;
import org.swiftboot.web.response.Response;
import org.swiftboot.web.response.ResponseBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @since 3.0
 */
public class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomOAuth2LoginSuccessHandler.class);

    @Resource
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

    @Resource
    private UserService userService;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oauthToken) {
            log.debug("Principle: " + oauthToken.getPrincipal());
            OAuth2User oauth2User = oauthToken.getPrincipal();
            Map<String, Object> userData = oauth2User.getAttribute("data");
            if (userData == null) {
                throw new ServletException("Failed to authenticate user since no user data returned");
            }
            String username = String.valueOf(userData.get("name"));
            String openId = String.valueOf(userData.get("open_id"));
            String platformUserId = String.valueOf(userData.get("user_id"));
            log.debug("User: " + username);
            log.debug("Open ID: " + openId);
            log.debug("Platform User ID: " + platformUserId);
            log.debug(oauthToken.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
            log.debug("OAuth token details: " + oauthToken.getDetails());

            OAuth2AuthorizedClient oaClient = oAuth2AuthorizedClientService.loadAuthorizedClient("feishu", authentication.getName());
            OAuth2AccessToken accessToken = oaClient.getAccessToken();
            OAuth2RefreshToken refreshToken = oaClient.getRefreshToken();
            String principalName = oaClient.getPrincipalName();
            TokenType tokenType = accessToken.getTokenType();
            Instant expiresAt = accessToken.getExpiresAt();

            log.info("authorized principle: %s".formatted(principalName));
            log.debug("token type: %s".formatted(tokenType));
            log.debug("access token from authorization server: %s".formatted(accessToken.getTokenValue()));
            log.debug("access tokens expires at: %s".formatted(expiresAt));
            if (refreshToken != null) {
                Instant refreshTokenExpiresAt = refreshToken.getExpiresAt();
                log.debug("refresh token from authorization server: %s".formatted(refreshToken.getTokenValue()));
                log.debug("refresh tokens expires at: %s".formatted(refreshTokenExpiresAt));
            }

            UserInfoDto userByOpenId = userService.findUserByOpenId(openId);
            if (userByOpenId == null) {
                userByOpenId = userService.createUserFromOAuth2(openId, username, accessToken, refreshToken);
            }

            AccessToken userAccessToken = jwtTokenProvider.generateAccessToken(userByOpenId.getId(), userByOpenId.getLoginName());
            RefreshToken userRefreshToken = jwtTokenProvider.generateRefreshToken(userByOpenId.getId());

            AuthenticatedDto authenticatedDto = new AuthenticatedDto(userAccessToken.tokenValue(), userAccessToken.expiresAt(),
                    userRefreshToken.tokenValue(), userRefreshToken.expiresAt());

            Response<Object> responseResult = new ResponseBuilder<>().result(authenticatedDto).build();
            String data = new Gson().toJson(responseResult);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            try (PrintWriter out = response.getWriter()) {
                out.write(data);
            }
        }
        else {
            // Default redirect
            response.sendRedirect("/"); // Or some other page
        }
    }
}
