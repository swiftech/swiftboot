package org.swiftboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.swiftboot.web.SpringBootEnv;

@SpringBootApplication
public class SwiftbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication myapp = new SpringApplication(SwiftbootDemoApplication.class);
        myapp.addListeners(new SpringBootEnv());
        myapp.run(args);
    }

}
