package org.swiftboot.demo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author swiftech
 **/
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"org.swiftboot.demo"})
public class SwiftbootDemoConfig {

}
