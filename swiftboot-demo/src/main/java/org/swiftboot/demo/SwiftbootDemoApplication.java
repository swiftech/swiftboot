package org.swiftboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.swiftboot.web.SpringBootEnv;

import java.util.Locale;

@SpringBootApplication
public class SwiftbootDemoApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        SpringApplication myapp = new SpringApplication(SwiftbootDemoApplication.class);
        myapp.addListeners(new SpringBootEnv());
        myapp.run(args);
    }

}
