package org.swiftboot.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.swiftboot.web.response.ResponseCode;

import java.io.IOException;

@Component
public class ErrorCode extends ResponseCode {

    public static String CODE_CUSTOMIZED_ERROR = "4001";
    public static String CODE_CUSTOMIZED_ERROR_WITH_PARAMS = "4002";

    @PostConstruct
    public void init() {
        try {
            super.loadFromClass(this.getClass());
        } catch (IllegalAccessException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
