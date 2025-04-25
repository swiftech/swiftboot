package org.swiftboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.swiftboot.web.SpringBootEnv;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Locale;

@SpringBootApplication
public class SwiftbootDemoAppServerApplication implements ApplicationListener<ApplicationEvent> {

    public static void main(String[] args) {
//        RedisServer redisServer;
//        try {
//            redisServer = RedisServer.newRedisServer()
//                    .port(6379)
//                    .build();
//            redisServer.start();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        SpringApplication myapp = new SpringApplication(SwiftbootDemoAppServerApplication.class);
        myapp.addListeners(new SpringBootEnv());
        myapp.run(args);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationStartingEvent) {

        }
    }
}
