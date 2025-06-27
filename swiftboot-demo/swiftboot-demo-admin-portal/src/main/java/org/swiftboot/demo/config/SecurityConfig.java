package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import org.swiftboot.security.JwtAuthenticationFilter;

/**
 * @since 3.0.0
 */
@Component
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    private UserDetailsService userDetailService;

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

        // Set permissions on endpoints
        httpSecurity.authorizeHttpRequests(authorize -> authorize
                // let auth methods go.
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/security/auth/*").permitAll()
                .requestMatchers("/admin/auth/*").permitAll()
                // permission required
                .requestMatchers("/admin/user/**").hasAuthority("admin:user")
                .requestMatchers("/goods/create/**").hasAuthority("goods:create")
                .requestMatchers("/goods/delete/**").hasAuthority("goods:delete")
                .requestMatchers("/goods/**").hasAuthority("goods")
                .requestMatchers("/order/**").hasAuthority("order")
                // others need authenticated.
                .anyRequest().authenticated()
        );
        httpSecurity.userDetailsService(userDetailService);
        return httpSecurity.build();
    }
}
