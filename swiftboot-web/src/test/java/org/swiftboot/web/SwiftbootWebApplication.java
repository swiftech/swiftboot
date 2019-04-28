package org.swiftboot.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.swiftboot.web.aspect.MyAspect;

/**
 * @author swiftech
 **/
@SpringBootApplication
//@Configuration
public class SwiftbootWebApplication {


    public static void main(String[] args) {
        SpringApplication.run(SwiftbootWebApplication.class, args);
    }

}
