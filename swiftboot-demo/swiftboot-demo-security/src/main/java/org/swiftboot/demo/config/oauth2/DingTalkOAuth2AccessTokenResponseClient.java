package org.swiftboot.demo.config.oauth2;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * DingTalk has different parameters to get Access Token.
 *
 * @since 3.0
 */
public class DingTalkOAuth2AccessTokenResponseClient implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private static final Logger log = LoggerFactory.getLogger(DingTalkOAuth2AccessTokenResponseClient.class);

    @Resource
    private final RestClient restClient;

    public DingTalkOAuth2AccessTokenResponseClient() {
        restClient = RestClient.builder().defaultStatusHandler(new OAuth2ErrorResponseErrorHandler()).build();
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("clientId", clientRegistration.getClientId());
        requestBody.put("clientSecret", clientRegistration.getClientSecret());
        requestBody.put("code", authorizationGrantRequest.getAuthorizationExchange().getAuthorizationResponse().getCode());
        requestBody.put("grantType", AuthorizationGrantType.AUTHORIZATION_CODE.getValue());

        URI tokenEndpointUri = URI.create(clientRegistration.getProviderDetails().getTokenUri());
        Map<String, Object> response = this.restClient.post()
                .uri(tokenEndpointUri)
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
        log.debug("return: : %s".formatted(new Gson().toJson(response)));
        return OAuth2AccessTokenResponse.withToken((String) response.get("accessToken"))
                .tokenType(OAuth2AccessToken.TokenType.BEARER)
                .expiresIn(((Number) response.get("expireIn")).longValue())
                .refreshToken((String) response.get("refreshToken"))
                .build();
    }
}
