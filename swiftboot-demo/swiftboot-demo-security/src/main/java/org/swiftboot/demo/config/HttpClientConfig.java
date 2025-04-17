package org.swiftboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

/**
 * Configure the HTTP client.
 *
 * @since 3.0
 */
@Component
public class HttpClientConfig {

    /**
     * Enable the Apache HTTP Component 5 to enable HTTP logging.
     *
     * @return
     */
    @Bean
    RestClient restClient() {
        return RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory()).build();
    }

    /**
     * Enable the Apache HTTP Component 5 to enable HTTP logging.
     * @return
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }

//    @Bean
//    public OkHttpClient okHttpClient() {
//        return new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .build();
//    }

}
