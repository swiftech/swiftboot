package org.swiftboot.demo;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;

/**
 * since 3.0.0
 */
@Tag(name = "App Secure Domain")
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppSecureController {

    private static final Logger log = LoggerFactory.getLogger(AppSecureController.class);

    @Operation(description = "Secure domain that needs authentication")
    @RequestMapping(value = "secure", method = RequestMethod.GET)
    public HttpResponse<String> appUserSign() {
        log.info("> /app/user/secure");
        return new HttpResponse<>("Authenticated");
    }

}
