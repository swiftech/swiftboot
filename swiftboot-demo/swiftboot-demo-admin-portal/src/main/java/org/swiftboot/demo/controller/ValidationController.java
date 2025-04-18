package org.swiftboot.demo.controller;

import jakarta.validation.Valid;
import org.swiftboot.demo.command.ValidationCommand;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;


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
    HttpResponse<Void> testPhoneNo(@Valid @RequestBody ValidationCommand command) {
        log.info(command.getPhoneNo());
        return new HttpResponse<>();
    }
}
