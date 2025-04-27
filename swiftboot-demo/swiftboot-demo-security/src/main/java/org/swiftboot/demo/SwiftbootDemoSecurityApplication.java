package org.swiftboot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.embedded.RedisServer;

import java.io.IOException;

@SpringBootApplication
public class SwiftbootDemoSecurityApplication {

    private static final Logger log = LoggerFactory.getLogger(SwiftbootDemoSecurityApplication.class);

    public static void main(String[] args) {
        log.trace("logging level: trace");
        log.debug("logging level: debug");
        log.info("logging level: info");
        log.warn("logging level: warn");
        log.error("logging level: error");
        String withEmbeddedRedis = System.getenv("WITH_EMBEDDED_REDIS");
        if (Boolean.parseBoolean(withEmbeddedRedis)) {
            RedisServer redisServer;
            try {
                redisServer = RedisServer.newRedisServer()
                        .port(6379)
                        .build();
                redisServer.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        SpringApplication.run(SwiftbootDemoSecurityApplication.class, args);
    }

}
