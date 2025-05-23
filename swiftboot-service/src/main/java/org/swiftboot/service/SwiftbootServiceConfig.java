package org.swiftboot.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.swiftboot.service.config.RedisConfigBean;
import org.swiftboot.service.config.SwiftbootServiceConfigBean;
import org.swiftboot.service.service.CaptchaService;
import org.swiftboot.service.service.impl.CaptchaServiceImpl;

/**
 * @author swiftech 2019-05-24
 **/
@Configuration
@EnableConfigurationProperties
public class SwiftbootServiceConfig {

    private static final Logger log = LoggerFactory.getLogger(SwiftbootServiceConfig.class);

    @Resource
    SwiftbootServiceConfigBean swiftbootServiceConfigBean;

    @Resource
    RedisConfigBean redisConfig;

//    @Value("swiftboot.service.redis.cluster")
//    String redisClusterUri;

//    @Bean
//    @ConditionalOnProperty(name = "spring.data.redis.host", havingValue = "")
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration();
//        redisStandaloneConfig.setHostName(redisConfig.getHost());
//        redisStandaloneConfig.setPort(redisConfig.getPort());
//        redisStandaloneConfig.setPassword(redisConfig.getPassword());
//        redisStandaloneConfig.setDatabase(0);
//        return new JedisConnectionFactory(redisStandaloneConfig);
//    }

    @Bean()
    public StringRedisTemplate stringRestTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Captcha 服务，依赖于 RedisService
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }

}
