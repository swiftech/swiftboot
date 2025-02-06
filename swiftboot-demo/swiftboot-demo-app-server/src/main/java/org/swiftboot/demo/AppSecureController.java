package org.swiftboot.demo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = {"App Secure Domain"})
@Controller
@RequestMapping("/app")
@ResponseBody
public class AppSecureController {

    private static final Logger log = LoggerFactory.getLogger(AppSecureController.class);

    @ApiOperation(notes = "Secure domain that needs authentication", value = "Secure domain that needs authentication")
    @RequestMapping(value = "secure", method = RequestMethod.GET)
    public HttpResponse<String> appUserSign() {
        log.info("> /app/user/secure");
        return new HttpResponse<>("Authenticated");
    }

}
