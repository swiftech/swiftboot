package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
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

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // Enable CORS and disable CSRF
        httpSecurity.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        // Set session management to stateless
        httpSecurity.sessionManagement(cfg -> {
//            cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
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
                // let auth methods go.
                .requestMatchers("/security/auth/logout").permitAll()
                .requestMatchers("/security/auth/logout_success").permitAll()
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/security/auth/*").permitAll() // allow authentication endpoints.
                // others need authenticated.
                .anyRequest().authenticated()
        );
        httpSecurity.userDetailsService(userDetailService);
        return httpSecurity.build();
    }

    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler logoutSuccessHandler = new SimpleUrlLogoutSuccessHandler();
        logoutSuccessHandler.setDefaultTargetUrl(baseUrl + "/security/auth/logout_success");
        return logoutSuccessHandler;
    }

}
