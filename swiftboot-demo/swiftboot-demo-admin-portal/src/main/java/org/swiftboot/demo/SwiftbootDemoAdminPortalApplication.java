package org.swiftboot.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.swiftboot.web.SpringBootEnv;

import java.util.Locale;

@SpringBootApplication
public class SwiftbootDemoAdminPortalApplication {

    public static void main(String[] args) {
        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
        SpringApplication myapp = new SpringApplication(SwiftbootDemoAdminPortalApplication.class);
        myapp.addListeners(new SpringBootEnv());
        myapp.run(args);
    }

}
