package org.swiftboot.demo;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "org.swiftboot.web",
        "org.swiftboot.data",
        "org.swiftboot.service",
        "org.swiftboot.security",
        "org.swiftboot.security.config",
        "org.swiftboot.common",
        "org.swiftboot.demo"
})
public class SwiftbootDemoSecurityConfig {

}
