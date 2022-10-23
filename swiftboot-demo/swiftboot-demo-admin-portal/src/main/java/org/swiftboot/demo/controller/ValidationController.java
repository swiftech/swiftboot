package org.swiftboot.demo.controller;

import org.swiftboot.demo.command.ValidationCommand;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.swiftboot.web.result.HttpResponse;

import javax.validation.Valid;

/**
 * @author allen
 */
@Api("测试Validation")
@Controller
@RequestMapping("/test/validation")
public class ValidationController {
    private final Logger log = LoggerFactory.getLogger(ValidationController.class);

    @RequestMapping(value = "phone_no", method = RequestMethod.POST)
    public
    @ResponseBody
    HttpResponse<Void> testPhoneNo(@Valid @RequestBody ValidationCommand command) {
        log.info(command.getPhoneNo());
        return new HttpResponse<>();
    }
}
