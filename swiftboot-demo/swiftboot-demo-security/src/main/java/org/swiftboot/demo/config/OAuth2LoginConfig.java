package org.swiftboot.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.demo.handler.CustomOAuth2LoginSuccessHandler;

/**
 * @since 3.0
 */
@Configuration
public class OAuth2LoginConfig {

    @Bean
    CustomOAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new CustomOAuth2LoginSuccessHandler();
    }

}
