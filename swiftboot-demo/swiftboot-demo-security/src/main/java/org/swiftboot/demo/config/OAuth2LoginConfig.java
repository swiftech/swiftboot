package org.swiftboot.demo.config;


import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.swiftboot.demo.dao.PlatformAuthorizationRepository;
import org.swiftboot.demo.dao.entity.PlatformAuthorization;
import org.swiftboot.demo.result.AuthResult;
import org.swiftboot.web.result.HttpResponse;
import org.swiftboot.web.result.ResponseBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class OAuth2LoginConfig {

    @Bean
    CustomOAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new CustomOAuth2LoginSuccessHandler();
    }

    public static class CustomOAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

        private static final Logger log = LoggerFactory.getLogger(CustomOAuth2LoginSuccessHandler.class);

        @Resource
        private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;

        @Resource
        private PlatformAuthorizationRepository platformAuthorizationRepository;

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

                PlatformAuthorization pa = platformAuthorizationRepository.findByOpenId(openId);
                if (pa == null) {
                    pa = new PlatformAuthorization();
                    pa.setOpenId(openId);
                    pa.setUserName(username);
                    pa.setAccessToken(accessToken.getTokenValue());

                    if (expiresAt != null) {
                        pa.setExpiresAt(expiresAt.toEpochMilli());
                    }
                    if (refreshToken != null) {
                        pa.setRefreshToken(refreshToken.getTokenValue());
                        if (refreshToken.getExpiresAt() != null) {
                            pa.setRefreshTokenExpiresAt(refreshToken.getExpiresAt().toEpochMilli());
                        }
                    }
                    platformAuthorizationRepository.save(pa);
                }

                // DANGER: Directly returning the token to the client
                AuthResult authResult = new AuthResult(pa.getAccessToken(), pa.getRefreshToken());
                authResult.setExpiresAt(pa.getExpiresAt());
                authResult.setRefreshToken(pa.getRefreshToken());
                HttpResponse<Object> responseResult = new ResponseBuilder<>().result(authResult).build();
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
}
