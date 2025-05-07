package org.swiftboot.demo.controller;

import jakarta.validation.Valid;
import org.swiftboot.demo.request.ValidationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.response.Response;


/**
 * @author swiftech
 */
@Tag(name = "测试Validation")
@Controller
@RequestMapping("/test/validation")
public class ValidationController {
    private static final Logger log = LoggerFactory.getLogger(ValidationController.class);

    @RequestMapping(value = "phone_no", method = RequestMethod.POST)
    public
    @ResponseBody
    Response<Void> testPhoneNo(@Valid @RequestBody ValidationRequest request) {
        log.info(request.getPhoneNo());
        return new Response<>();
    }
}
