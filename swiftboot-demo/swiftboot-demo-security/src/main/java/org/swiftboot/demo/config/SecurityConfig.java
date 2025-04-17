package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.swiftboot.common.auth.JwtConfigBean;
import org.swiftboot.demo.config.OAuth2LoginConfig.CustomOAuth2LoginSuccessHandler;
import org.swiftboot.demo.config.oauth2.OAuth2AccessTokenResponseClientRouter;
import org.swiftboot.security.JwtAuthenticationFilter;
import org.swiftboot.security.RevokedTokenDao;

/**
 * @author swiftech
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private UserDetailsService userDetailService;

    // for logout redirection.
    @Value("${swiftboot.demo.security.baseUrl}")
    private String baseUrl;

    @Resource
    private RevokedTokenDao revokedTokenDao;

    @Resource
    private JwtConfigBean jwtConfigBean;

    @Resource
    private CustomOAuth2LoginSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Enable CORS and disable CSRF
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        // Set session management to stateless
        httpSecurity.sessionManagement(cfg -> {
            cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            cfg.disable();
        });

        httpSecurity.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);

        // Set unauthorized requests exception handler
        httpSecurity.exceptionHandling(cfg -> {
//            cfg.accessDeniedHandler(swiftbootAccessDeniedHandler);
//            cfg.authenticationEntryPoint(delegatedAuthenticationEntryPoint);
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/security/auth/logout"); // override the default URL to do logout.
            logout.logoutSuccessHandler(logoutSuccessHandler()); // handle the logout success
        });

        // Set permissions on endpoints
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                        // allow authentication endpoints.
                        .requestMatchers("/test/**").permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs.yaml").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        // OAuth2
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers("/favicon.ico").permitAll()
                        //
                        .requestMatchers("/security/auth/logout").permitAll()
                        .requestMatchers("/security/auth/logout_success").permitAll()
                        .requestMatchers("/error/**").permitAll()
                        // others need authenticated.
                        .anyRequest().authenticated()
        );
        httpSecurity.userDetailsService(userDetailService);
        // customized oauth2 login
        httpSecurity.oauth2Login(oauth2Login -> {
            oauth2Login.successHandler(oAuth2AuthenticationSuccessHandler);
            oauth2Login.tokenEndpoint(tokenEndpointConfig -> {
                tokenEndpointConfig.accessTokenResponseClient(oAuth2AccessTokenResponseClientRouter());
            });
        });
//        httpSecurity.oauth2ResourceServer(resourceServer -> {
//            resourceServer.jwt(jwt -> {
//                Customizer.withDefaults();
//            });
//        });
        return httpSecurity.build();
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl(baseUrl + "/security/auth/logout_success");
        return logoutSuccessHandler;
    }

//    @Bean
//    public JwtDecoder jwtDecoder() {
//        byte[] secretData = jwtConfigBean.getSecret().getBytes();
//        SecretKey secretKey = new SecretKeySpec(secretData, "HmacSHA256");
//        return NimbusJwtDecoder.withSecretKey(secretKey).build();
//    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClientRouter() {
        return new OAuth2AccessTokenResponseClientRouter();
    }

}
