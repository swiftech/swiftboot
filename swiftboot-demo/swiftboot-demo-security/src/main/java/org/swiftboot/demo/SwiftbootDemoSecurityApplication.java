package org.swiftboot.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SwiftbootDemoSecurityApplication {

    private static final Logger log = LoggerFactory.getLogger(SwiftbootDemoSecurityApplication.class);

    public static void main(String[] args) {
        log.trace("logging level: trace");
        log.debug("logging level: debug");
        log.info("logging level: info");
        log.warn("logging level: warn");
        log.error("logging level: error");
        SpringApplication.run(SwiftbootDemoSecurityApplication.class, args);
    }

}
