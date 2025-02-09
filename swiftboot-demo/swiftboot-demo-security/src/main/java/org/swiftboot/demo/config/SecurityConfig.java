package org.swiftboot.demo.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/js/**", "/css/**");
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        // Set session management to stateless
        http.sessionManagement(cfg -> {
//            cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            cfg.disable();
        });

        http.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class);

        // Set unauthorized requests exception handler
        http.exceptionHandling(cfg -> {
//            cfg.accessDeniedHandler(swiftbootAccessDeniedHandler);
//            cfg.authenticationEntryPoint(delegatedAuthenticationEntryPoint);
        });

        // Set permissions on endpoints
        http.authorizeHttpRequests(authorize -> authorize
                // let auth methods go.
                .requestMatchers("/error/**").permitAll()
                .requestMatchers("/security/auth/*").permitAll()
                // others need authenticated.
                .anyRequest().authenticated()
        );
        http.userDetailsService(userDetailService);
        return http.build();
    }


//    @Bean
//    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
//        return tomcatServletWebServerFactory ->
//                tomcatServletWebServerFactory.addContextCustomizers(context ->
//                        context.setCookieProcessor(new LegacyCookieProcessor()));
//    }

}
