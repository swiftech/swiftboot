package org.swiftboot.demo.config.oauth2;

import org.springframework.security.oauth2.client.endpoint.*;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Route to different Access Token response client, if not matched, use the default one.
 *
 * @since 3.0
 */
public class OAuth2AccessTokenResponseClientRouter implements OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> {

    private final Map<String, OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest>> clients;

    public OAuth2AccessTokenResponseClientRouter() {
        this.clients = new HashMap<>();
        this.clients.put("dingtalk", new DingTalkOAuth2AccessTokenResponseClient());
        this.clients.put("feishu", new RestClientAuthorizationCodeTokenResponseClient());
    }

    @Override
    public OAuth2AccessTokenResponse getTokenResponse(OAuth2AuthorizationCodeGrantRequest authorizationGrantRequest) {
        ClientRegistration clientRegistration = authorizationGrantRequest.getClientRegistration();
        String registrationId = clientRegistration.getRegistrationId();
        OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> client = this.clients.get(registrationId);
        if (client == null) {
            client = new RestClientAuthorizationCodeTokenResponseClient();
        }
        return client.getTokenResponse(authorizationGrantRequest);
    }
}
