package org.swiftboot.service;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.swiftboot.service.config.RedisConfigBean;
import org.swiftboot.service.config.SwiftbootServiceConfigBean;
import org.swiftboot.service.service.CaptchaService;
import org.swiftboot.service.service.RedisService;
import org.swiftboot.service.service.impl.CaptchaServiceImpl;
import org.swiftboot.service.service.impl.RedisClusterServiceImpl;
import org.swiftboot.service.service.impl.RedisServiceImpl;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

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

    @Value("swiftboot.service.redis.cluster")
    String redisClusterUri;

    @Bean
    @ConditionalOnProperty("spring.data.redis.host")
    public RedisConnectionFactory redisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean()
    @ConditionalOnProperty("spring.data.redis.host")
    public StringRedisTemplate stringRestTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @ConditionalOnProperty("spring.data.redis.host")
    public RedisService redisService() {
        log.info("default redis service implementation");
        return new RedisServiceImpl();
    }

    @Bean
    @ConditionalOnProperty("swiftboot.service.redis.cluster")
    public JedisCluster redisCluster() {
        log.info("clustered redis service implementation");
        String[] serverArray = redisClusterUri.split(",");
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.parseInt(ipPortPair[1].trim())));
        }
        return new JedisCluster(nodes);
    }

    /**
     * 依赖于配置了 JedisCluster
     *
     * @return
     */
    @Bean
    @ConditionalOnBean(JedisCluster.class)
    public RedisClusterServiceImpl redisClusterService() {
        return new RedisClusterServiceImpl();
    }

    /**
     * Captcha 服务，依赖于 RedisService
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty("swiftboot.captcha")
    @ConditionalOnBean(RedisService.class)
    public CaptchaService captchaService() {
        return new CaptchaServiceImpl();
    }

}
