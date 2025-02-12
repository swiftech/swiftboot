package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.swiftboot.security.JwtAuthenticationFilter;

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

//    @Resource
//    private SwiftbootAuthenticationEntryPoint swiftbootAuthenticationEntryPoint;

//    @Resource
//    private SwiftbootAccessDeniedHandler swiftbootAccessDeniedHandler;

//    @Resource
//    @Qualifier("delegatedAuthenticationEntryPoint")
//    private DelegatedAuthenticationEntryPoint delegatedAuthenticationEntryPoint;

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
                // others need authenticated.
                .anyRequest().authenticated()
        );
        httpSecurity.userDetailsService(userDetailService);
        return httpSecurity.build();
    }


//    @Bean
//    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
//        return tomcatServletWebServerFactory ->
//                tomcatServletWebServerFactory.addContextCustomizers(context ->
//                        context.setCookieProcessor(new LegacyCookieProcessor()));
//    }

}
